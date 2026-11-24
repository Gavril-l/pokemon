package com.uabc.poo2.controlador;

import com.uabc.poo2.audio.Musica;
import com.uabc.poo2.audio.Musica.Pista;
import com.uabc.poo2.modelo.MotorJuego;
import com.uabc.poo2.modelo.MotorJuego.EstadoJugador;
import com.uabc.poo2.modelo.MotorJuego.ResultadoRonda;
import com.uabc.poo2.modelo.Pokemon;
import com.uabc.poo2.red.Cliente;
import com.uabc.poo2.red.Server;
import com.uabc.poo2.red.protocolo.Mensaje;
import com.uabc.poo2.red.protocolo.TipoDeMensaje;
import com.uabc.poo2.util.Utilidades;
import com.uabc.poo2.vista.componentes.TarjetaPokemon;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class PartidaController implements Initializable {

  private static final int SEGUNDOS_POR_RONDA = 20;

  // Medidas de diseño (a escala 1.0). Las cartas miden 750 x 1050, o sea alto = ancho * 1.4
  private static final double PROPORCION_CARTA = 1.4;
  private static final double ANCHO_CARTA_MANO = 140;
  private static final double ANCHO_CARTA_CENTRO = 190;
  private static final double SEPARACION_MANO = 14;
  private static final double SEPARACION_CENTRO = 60;

  //para calcular la escala segun el tamaño de la ventana:
  //lo que ocupa la pantalla sin contar las cartas (titulos, botones, margenes)...
  private static final double ALTO_SIN_CARTAS = 190;
  //lo que ocupan las cartas a escala 1.0 (carta del centro + mano + titulos)...
  private static final double ALTO_CARTAS_BASE = (ANCHO_CARTA_CENTRO + ANCHO_CARTA_MANO) * PROPORCION_CARTA + 60;
  //y lo que mide la mano de 5 cartas a escala 1.0 (con margenes)
  private static final double ANCHO_MANO_BASE = 5 * ANCHO_CARTA_MANO + 4 * SEPARACION_MANO + 40;
  private static final double ESCALA_MIN = 0.5;
  private static final double ESCALA_MAX = 2.2;

  @FXML
  private Label lblSala;

  @FXML
  private Label lblEstado;

  @FXML
  private Button btnVolver;

  @FXML
  private BorderPane raiz;

  @FXML
  private Label lblCronometro;

  @FXML
  private Label lblMarcadorLocal;

  @FXML
  private Label lblMarcadorOponente;

  @FXML
  private Label lblTituloLocal;

  @FXML
  private Label lblTituloOponente;

  @FXML
  private HBox zonaCentro;

  @FXML
  private StackPane contenedorCartaLocal;

  @FXML
  private StackPane contenedorCartaOponente;

  @FXML
  private HBox contenedorMano;

  // escala = cuanto crecen o se encogen las cartas segun el tamaño de la ventana
  private final DoubleProperty escala = new SimpleDoubleProperty(1);
  private final DoubleProperty escalaFuente = new SimpleDoubleProperty(1);
  private final DoubleProperty anchoCartaMano = new SimpleDoubleProperty(ANCHO_CARTA_MANO);
  private final DoubleProperty anchoCartaCentro = new SimpleDoubleProperty(ANCHO_CARTA_CENTRO);

  private final MotorJuego motor = new MotorJuego();
  private final EstadoJugador estadoLocal = new EstadoJugador();
  private final EstadoJugador estadoOponente = new EstadoJugador();

  private Cliente cliente;
  private Server servidor;
  private String nombreSala;
  private String nombreJugador = "Jugador";
  private String nombreOponente = "Oponente";

  private List<Pokemon> manoJugador = new ArrayList<>();
  private Pokemon cartaElegidaLocal;
  private Pokemon cartaElegidaOponente;
  private boolean cartaLocalConfirmada;
  private boolean cartaOponenteConfirmada;
  private int segundosRestantes = SEGUNDOS_POR_RONDA;
  private Timeline cronometro;
  private boolean partidaTerminada;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    Musica.reproducir(Pista.PARTIDA);
    configurarEscala();
  }

  //todo el tablero depende de la escala, y la escala del tamaño de la ventana
  //al agrandar o achicar la ventana las cartas, los espacios y las letras se
  //ajustan solos y se quedan en su lugar.
  private void configurarEscala() {
    escala.bind(Bindings.createDoubleBinding(() -> {
      double porAlto = (raiz.getHeight() - ALTO_SIN_CARTAS) / ALTO_CARTAS_BASE;
      double porAncho = raiz.getWidth() / ANCHO_MANO_BASE;
      return Math.max(ESCALA_MIN, Math.min(ESCALA_MAX, Math.min(porAlto, porAncho)));
    }, raiz.widthProperty(), raiz.heightProperty()));

    // las letras crecen menos que las cartas para que no se vean desproporcionadas
    escalaFuente.bind(Bindings.createDoubleBinding(
        () -> Math.max(0.9, Math.min(1.5, escala.get())), escala));

    anchoCartaMano.bind(escala.multiply(ANCHO_CARTA_MANO));
    anchoCartaCentro.bind(escala.multiply(ANCHO_CARTA_CENTRO));

    contenedorMano.spacingProperty().bind(escala.multiply(SEPARACION_MANO));
    contenedorMano.minHeightProperty().bind(anchoCartaMano.multiply(PROPORCION_CARTA));
    zonaCentro.spacingProperty().bind(escala.multiply(SEPARACION_CENTRO));
    ligarTamanoContenedor(contenedorCartaLocal);
    ligarTamanoContenedor(contenedorCartaOponente);

    ligarFuente(lblSala, 16, true);
    ligarFuente(lblCronometro, 16, true);
    ligarFuente(lblEstado, 14, false);
    ligarFuente(lblMarcadorLocal, 13, false);
    ligarFuente(lblMarcadorOponente, 13, false);
    ligarFuente(lblTituloLocal, 14, true);
    ligarFuente(lblTituloOponente, 14, true);
  }

  private void ligarTamanoContenedor(StackPane contenedor) {
    DoubleBinding alto = anchoCartaCentro.multiply(PROPORCION_CARTA);
    contenedor.minWidthProperty().bind(anchoCartaCentro);
    contenedor.prefWidthProperty().bind(anchoCartaCentro);
    contenedor.maxWidthProperty().bind(anchoCartaCentro);
    contenedor.minHeightProperty().bind(alto);
    contenedor.prefHeightProperty().bind(alto);
    contenedor.maxHeightProperty().bind(alto);
  }

  private void ligarFuente(Label etiqueta, double tamanoBase, boolean negrita) {
    String formato = "-fx-font-size: %.1fpx;" + (negrita ? " -fx-font-weight: bold;" : "");
    etiqueta.styleProperty().bind(escalaFuente.multiply(tamanoBase).asString(Locale.US, formato));
  }

  public void inicializarPartida(Cliente cliente, Server servidor, String nombreSala) {
    this.cliente = cliente;
    this.servidor = servidor;
    this.nombreSala = nombreSala;
    this.nombreJugador = (servidor != null) ? "Host" : "Jugador";
    this.nombreOponente = (servidor != null) ? "Cliente" : "Host";

    if (lblSala != null) {
      lblSala.setText("Sala: " + nombreSala);
    }
    if (lblEstado != null) {
      String rol = (servidor != null) ? "Host + Cliente conectado" : "Cliente conectado";
      lblEstado.setText("Estado: " + rol);
    }

    if (this.servidor != null) {
      this.servidor.setOnMensajeRecibido(this::recibirMensajeRed);
    }
    if (this.cliente != null) {
      this.cliente.setOnMensajeRecibido(this::recibirMensajeRed);
      this.cliente.iniciarEscucha();
    }

    this.manoJugador = motor.crearManoInicial();
    renderizarMano();
    actualizarMarcadores();
    iniciarNuevaRonda();
  }

  public void iniciarNuevaRonda() {
    if (partidaTerminada) {
      return;
    }

    detenerCronometro();
    cartaElegidaLocal = null;
    cartaElegidaOponente = null;
    cartaLocalConfirmada = false;
    cartaOponenteConfirmada = false;
    segundosRestantes = SEGUNDOS_POR_RONDA;

    limpiarCentroMesa();
    setManoHabilitada(true);
    actualizarEtiquetaCronometro();

    if (lblEstado != null) {
      lblEstado.setText("Elige una carta (20s)");
    }

    cronometro = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
      segundosRestantes--;
      actualizarEtiquetaCronometro();

      if (segundosRestantes <= 0) {
        detenerCronometro();
        if (!cartaLocalConfirmada) {
          Pokemon automatica = motor.elegirCartaAleatoria(manoJugador);
          confirmarCartaLocal(automatica);
        }
      }
    }));
    cronometro.setCycleCount(SEGUNDOS_POR_RONDA);
    cronometro.play();
  }

  private void confirmarCartaLocal(Pokemon carta) {
    if (partidaTerminada || cartaLocalConfirmada || carta == null) {
      return;
    }

    cartaLocalConfirmada = true;
    cartaElegidaLocal = carta;
    setManoHabilitada(false);

    mostrarCartaEnCentro(contenedorCartaLocal, cartaElegidaLocal, false);

    if (lblEstado != null && !cartaOponenteConfirmada) {
      lblEstado.setText("Carta bloqueada. Esperando al oponente...");
    }

    enviarMensaje(new Mensaje(TipoDeMensaje.SELECCIONAR_CARTA, nombreJugador, "CARTA_ELEGIDA", cartaElegidaLocal));
    evaluarSiAmbosListos();
  }

  public void recibirMensajeRed(Mensaje mensaje) {
    if (mensaje == null || mensaje.getTipo() == null) {
      return;
    }

    Platform.runLater(() -> {
      switch (mensaje.getTipo()) {
        case SELECCIONAR_CARTA -> {
          if (mensaje.getRemitente() != null && !mensaje.getRemitente().equals(nombreJugador)) {
            nombreOponente = mensaje.getRemitente();
            actualizarMarcadores();
          }
          if (!cartaOponenteConfirmada) {
            cartaOponenteConfirmada = true;
            cartaElegidaOponente = mensaje.getPokemon();
            mostrarCartaEnCentro(contenedorCartaOponente, cartaElegidaOponente, true);
            if (cartaLocalConfirmada) {
              evaluarSiAmbosListos();
            } else if (lblEstado != null) {
              lblEstado.setText("El oponente ya eligio su carta. ¡Elige la tuya!");
            }
          }
        }
        case INICIO_RONDA -> iniciarNuevaRonda();
        case FIN_PARTIDA -> {
          partidaTerminada = true;
          detenerCronometro();
          setManoHabilitada(false);
          if (lblEstado != null && mensaje.getContenido() != null) {
            lblEstado.setText(mensaje.getContenido());
          }
        }
        default -> {
        }
      }
    });
  }

  private void evaluarSiAmbosListos() {
    if (!cartaLocalConfirmada || !cartaOponenteConfirmada) {
      return;
    }

    detenerCronometro();

    mostrarCartaEnCentro(contenedorCartaLocal, cartaElegidaLocal, false);
    mostrarCartaEnCentro(contenedorCartaOponente, cartaElegidaOponente, false);

    ResultadoRonda resultado = motor.resolverRonda(cartaElegidaLocal, cartaElegidaOponente);
    String resumen;

    if (resultado == ResultadoRonda.GANA_JUGADOR_1) {
      estadoLocal.registrarVictoria(cartaElegidaLocal != null ? cartaElegidaLocal.getTipo() : null);
      resumen = "¡Ganaste la ronda con " + nombreCarta(cartaElegidaLocal) + "!";
    } else if (resultado == ResultadoRonda.GANA_JUGADOR_2) {
      estadoOponente.registrarVictoria(cartaElegidaOponente != null ? cartaElegidaOponente.getTipo() : null);
      resumen = nombreOponente + " gano la ronda con " + nombreCarta(cartaElegidaOponente) + ".";
    } else {
      resumen = "Ronda empatada. Nadie suma victoria.";
    }

    // Reemplaza la carta jugada en su misma posicion y refresca la mano al cerrar
    // la ronda
    motor.usarYReponerCarta(manoJugador, cartaElegidaLocal);
    renderizarMano();
    actualizarMarcadores();

    if (estadoLocal.haGanadoPartida()) {
      partidaTerminada = true;
      String mensajeFinal = "¡Victoria de " + nombreJugador + "! " + resumen;
      if (lblEstado != null) {
        lblEstado.setText(mensajeFinal);
      }
      Mensaje fin = new Mensaje(TipoDeMensaje.FIN_PARTIDA, nombreJugador, mensajeFinal);
      fin.setRondasGanadas(estadoLocal.getRondasGanadas());
      fin.setVictoriasMismoTipo(estadoLocal.getMaxVictoriasMismoTipo());
      enviarMensaje(fin);
      return;
    }

    if (estadoOponente.haGanadoPartida()) {
      partidaTerminada = true;
      String mensajeFinal = "Derrota: " + nombreOponente + " gano la partida. " + resumen;
      if (lblEstado != null) {
        lblEstado.setText(mensajeFinal);
      }
      Mensaje fin = new Mensaje(TipoDeMensaje.FIN_PARTIDA, nombreOponente, mensajeFinal);
      fin.setRondasGanadas(estadoOponente.getRondasGanadas());
      fin.setVictoriasMismoTipo(estadoOponente.getMaxVictoriasMismoTipo());
      enviarMensaje(fin);
      return;
    }

    if (lblEstado != null) {
      lblEstado.setText(resumen + " Siguiente ronda en 3s...");
    }

    Timeline pausaSiguienteRonda = new Timeline(new KeyFrame(Duration.seconds(3), e -> iniciarNuevaRonda()));
    pausaSiguienteRonda.setCycleCount(1);
    pausaSiguienteRonda.play();
  }

  private void renderizarMano() {
    if (contenedorMano == null) {
      return;
    }
    contenedorMano.getChildren().clear();

    for (Pokemon carta : manoJugador) {
      Node cartaVisual = crearVistaCarta(carta, anchoCartaMano);
      boolean habilitada = !cartaLocalConfirmada && !partidaTerminada;
      cartaVisual.setDisable(!habilitada);
      cartaVisual.setCursor(habilitada ? Cursor.HAND : Cursor.DEFAULT);
      cartaVisual.setOnMouseClicked(e -> {
        if (!cartaLocalConfirmada && !partidaTerminada) {
          confirmarCartaLocal(carta);
        }
      });
      contenedorMano.getChildren().add(cartaVisual);
    }
  }

  //la carta mide lo que diga "ancho" que sigue a la ventana y el alto sale de
  //la proporcion, asi la imagen no se deforma ni se pierde calidad
  private Node crearVistaCarta(Pokemon carta, DoubleProperty ancho) {
    TarjetaPokemon tarjeta = new TarjetaPokemon(carta, TarjetaPokemon.RESOLUCION_ALTA);
    tarjeta.setMostrarNombre(false);
    tarjeta.ligarAncho(ancho);
    tarjeta.setStyle("-fx-background-color: transparent; -fx-border-width: 0; -fx-padding: 0;");
    return tarjeta;
  }

  private StackPane crearCartaBocaAbajo(DoubleProperty ancho) {
    DoubleBinding alto = ancho.multiply(PROPORCION_CARTA);

    StackPane reverso = new StackPane();
    reverso.minWidthProperty().bind(ancho);
    reverso.prefWidthProperty().bind(ancho);
    reverso.maxWidthProperty().bind(ancho);
    reverso.minHeightProperty().bind(alto);
    reverso.prefHeightProperty().bind(alto);
    reverso.maxHeightProperty().bind(alto);
    reverso.setStyle(
        "-fx-background-color: linear-gradient(to bottom right, #1e3a8a, #0f172a);"
            + "-fx-background-radius: 12;"
            + "-fx-border-color: #facc15;"
            + "-fx-border-width: 3;"
            + "-fx-border-radius: 12;");

    Label emblema = new Label("?");
    emblema.styleProperty().bind(ancho.multiply(0.225).asString(Locale.US,
        "-fx-text-fill: #facc15; -fx-font-size: %.0fpx; -fx-font-weight: bold;"));
    reverso.getChildren().add(emblema);
    return reverso;
  }

  private void setManoHabilitada(boolean habilitada) {
    if (contenedorMano == null) {
      return;
    }
    for (Node nodo : contenedorMano.getChildren()) {
      nodo.setDisable(!habilitada);
      nodo.setCursor(habilitada ? Cursor.HAND : Cursor.DEFAULT);
    }
  }

  private void mostrarCartaEnCentro(StackPane contenedor, Pokemon carta, boolean oculta) {
    if (contenedor == null) {
      return;
    }
    contenedor.getChildren().clear();

    if (oculta) {
      contenedor.getChildren().add(crearCartaBocaAbajo(anchoCartaCentro));
      return;
    }

    if (carta != null) {
      contenedor.getChildren().add(crearVistaCarta(carta, anchoCartaCentro));
    }
  }

  private void limpiarCentroMesa() {
    if (contenedorCartaLocal != null) {
      contenedorCartaLocal.getChildren().clear();
    }
    if (contenedorCartaOponente != null) {
      contenedorCartaOponente.getChildren().clear();
    }
  }

  private void actualizarEtiquetaCronometro() {
    if (lblCronometro != null) {
      lblCronometro.setText("Tiempo: " + segundosRestantes + "s");
    }
  }

  private void actualizarMarcadores() {
    if (lblMarcadorLocal != null) {
      lblMarcadorLocal.setText(nombreJugador + " -> Rondas: " + estadoLocal.getRondasGanadas()
          + "/5 | Mismo tipo: " + estadoLocal.getMaxVictoriasMismoTipo() + "/3");
    }
    if (lblMarcadorOponente != null) {
      lblMarcadorOponente.setText(nombreOponente + " -> Rondas: " + estadoOponente.getRondasGanadas()
          + "/5 | Mismo tipo: " + estadoOponente.getMaxVictoriasMismoTipo() + "/3");
    }
  }

  private void detenerCronometro() {
    if (cronometro != null) {
      cronometro.stop();
      cronometro = null;
    }
  }

  private void enviarMensaje(Mensaje mensaje) {
    if (mensaje == null) {
      return;
    }
    if (servidor != null) {
      servidor.enviarAlCliente(mensaje);
    } else if (cliente != null) {
      cliente.enviarMensaje(mensaje);
    }
  }

  private String nombreCarta(Pokemon p) {
    return p != null ? p.getNombre() : "Carta";
  }

  @FXML
  void onVolverClick(ActionEvent event) {
    detenerCronometro();
    partidaTerminada = true;
    // al salir de la partida se cierra todo, si no los puertos 1234 (TCP) y
    // 4445 (UDP) se quedan ocupados y no se puede volver a crear una sala.
    if (cliente != null) {
      cliente.desconectar();
    }
    if (servidor != null) {
      servidor.detener();
    }

    FXMLLoader loader = Utilidades.cambiarPantalla(event, "MatchMaking - Pokémon Card-Jitsu", "servers.fxml");
    if (loader == null) {
      return;
    }

    // Cliente nuevo: el anterior ya esta desconectado
    ServersPageControlador controlador = loader.getController();
    controlador.setCliente(new Cliente());
  }
}