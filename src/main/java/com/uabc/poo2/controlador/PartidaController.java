package com.uabc.poo2.controlador;

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
import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class PartidaController {

  private static final int SEGUNDOS_POR_RONDA = 20;
  private static final double ANCHO_CARTA_MANO = 135;
  private static final double ALTO_CARTA_MANO = 185;
  private static final double ANCHO_CARTA_CENTRO = 160;
  private static final double ALTO_CARTA_CENTRO = 215;

  @FXML
  private Label lblSala;

  @FXML
  private Label lblEstado;

  @FXML
  private Button btnVolver;

  private Label lblCronometro;
  private Label lblMarcadorLocal;
  private Label lblMarcadorOponente;
  private StackPane contenedorCartaLocal;
  private StackPane contenedorCartaOponente;
  private HBox contenedorMano;
  private boolean tableroMontado = false;

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

    montarTableroSiEsPosible();
    this.manoJugador = motor.crearManoInicial();
    renderizarMano();
    actualizarMarcadores();
    iniciarNuevaRonda();
  }

  private void montarTableroSiEsPosible() {
    if (tableroMontado || lblEstado == null) {
      return;
    }
    Parent parent = lblEstado.getParent();
    while (parent != null && !(parent instanceof Pane)) {
      parent = parent.getParent();
    }
    if (!(parent instanceof Pane pane)) {
      return;
    }

    lblCronometro = new Label("Tiempo: " + SEGUNDOS_POR_RONDA + "s");
    lblCronometro.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");

    lblMarcadorLocal = new Label();
    lblMarcadorOponente = new Label();
    HBox marcadores = new HBox(24, lblMarcadorLocal, lblMarcadorOponente);
    marcadores.setAlignment(Pos.CENTER);

    contenedorCartaLocal = new StackPane();
    contenedorCartaLocal.setAlignment(Pos.CENTER);
    contenedorCartaLocal.setMinSize(ANCHO_CARTA_CENTRO, ALTO_CARTA_CENTRO);
    contenedorCartaLocal.setPrefSize(ANCHO_CARTA_CENTRO, ALTO_CARTA_CENTRO);

    contenedorCartaOponente = new StackPane();
    contenedorCartaOponente.setAlignment(Pos.CENTER);
    contenedorCartaOponente.setMinSize(ANCHO_CARTA_CENTRO, ALTO_CARTA_CENTRO);
    contenedorCartaOponente.setPrefSize(ANCHO_CARTA_CENTRO, ALTO_CARTA_CENTRO);

    HBox zonaCentro = new HBox(50,
        new VBox(4, posicionarCentro(new Label("Tu carta")), contenedorCartaLocal),
        new VBox(4, posicionarCentro(new Label("Oponente")), contenedorCartaOponente));
    zonaCentro.setAlignment(Pos.CENTER);

    contenedorMano = new HBox(14);
    contenedorMano.setAlignment(Pos.CENTER);
    contenedorMano.setPadding(new Insets(0, 10, 24, 10));

    VBox zonaJuego = new VBox(8, lblCronometro, marcadores, zonaCentro, contenedorMano);
    zonaJuego.setAlignment(Pos.TOP_CENTER);
    zonaJuego.setPadding(new Insets(2, 10, 16, 10));

    pane.getChildren().add(zonaJuego);
    tableroMontado = true;
  }

  private VBox posicionarCentro(Label titulo) {
    titulo.setMaxWidth(Double.MAX_VALUE);
    titulo.setAlignment(Pos.CENTER);
    return new VBox(titulo);
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

    // Reemplaza la carta jugada en su misma posicion y refresca la mano al cerrar la ronda
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
      Node cartaVisual = crearVistaCarta(carta, ANCHO_CARTA_MANO, ALTO_CARTA_MANO);
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

  private Node crearVistaCarta(Pokemon carta, double ancho, double alto) {
    TarjetaPokemon tarjeta = new TarjetaPokemon(carta);
    tarjeta.setMostrarNombre(false);
    tarjeta.setTamanoImagen(ancho, alto, false);
    tarjeta.setStyle("-fx-background-color: transparent; -fx-border-width: 0; -fx-padding: 0;");
    return tarjeta;
  }

  private StackPane crearCartaBocaAbajo(double ancho, double alto) {
    StackPane reverso = new StackPane();
    reverso.setPrefSize(ancho, alto);
    reverso.setMinSize(ancho, alto);
    reverso.setMaxSize(ancho, alto);
    reverso.setStyle(
        "-fx-background-color: linear-gradient(to bottom right, #1e3a8a, #0f172a);"
            + "-fx-background-radius: 12;"
            + "-fx-border-color: #facc15;"
            + "-fx-border-width: 3;"
            + "-fx-border-radius: 12;");

    Label emblema = new Label("?");
    emblema.setStyle("-fx-text-fill: #facc15; -fx-font-size: 36px; -fx-font-weight: bold;");
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
      contenedor.getChildren().add(crearCartaBocaAbajo(ANCHO_CARTA_CENTRO, ALTO_CARTA_CENTRO));
      return;
    }

    if (carta != null) {
      contenedor.getChildren().add(crearVistaCarta(carta, ANCHO_CARTA_CENTRO, ALTO_CARTA_CENTRO));
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