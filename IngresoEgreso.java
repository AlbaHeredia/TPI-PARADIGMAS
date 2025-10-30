import java.time.LocalDateTime;
import java.util.List;

public class IngresoEgreso {
    private int numeroTransaccion;
    private LocalDateTime fechaYhoraIngreso;
    private LocalDateTime fechaYhoraEgreso;
    private String dominio;
    private CobroEstacionamiento cobro;
    private Lugar lugar;
    private boolean esInvitado;
    private boolean esExterno;
    private Cuenta cuenta;

    public IngresoEgreso(int numeroTransacci0n, String fechaYhoraIngreso, String fechaYhoraEgreso, String dominio, CobroEstacionamiento cobro, Lugar lugar, Cuenta cuenta){
        this.numeroTransaccion = numeroTransaccion;
        this.fechaYhoraIngreso = fechaYhoraIngreso;
        this.fechaYhoraEgreso = fechaYhoraEgreso;
        this.dominio = dominio;
        this.cobro = cobro;
        this.lugar = lugar;
        this.cuenta = cuenta;
    }

    public IngresoEgreso() {}

    public int getNumeroTransaccion() {
        return numeroTransaccion;
    }

    public LocalDateTime getFechaYHoraIngreso() {
        return fechaYhoraIngreso;
    }

    public LocalDateTime getFechaYHoraEgreso() {
        return fechaYhoraEgreso;
    }

    public String getDominio() {
        return dominio;
    }

    public CobroEstacionamiento getCobro() {
        return cobro;
    }

    public Lugar getLugar() {
        return lugar;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }


    public void setNumeroTransaccion(int numeroTransaccion) {
        this.numeroTransaccion = numeroTransaccion;
    }

    public void setFechaYHoraIngreso(LocalDateTime fechaYHoraIngreso) {
        this.fechaYhoraIngreso = fechaYHoraIngreso;
    }

    public void setFechaYHoraEgreso(LocalDateTime fechaYHoraEgreso) {
        this.fechaYHoraEgreso = fechaYHoraEgreso;
    }

    public void setDominio(String dominio) {
        this.dominio = dominio;
    }

    public void setCobro(CobroEstacionamiento cobro) {
        this.cobro = cobro;
    }

    public void setLugar(Lugar lugar) {
        this.lugar = lugar;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }
    
    public void registrarIngreso(List<Lugar> lugaresLibres, Cuenta cuenta, String dominio, List<TipoUsuario> tiposUsuarios) {
        
        if (!cuenta.estaActiva()) {
            System.out.println("Error: La cuenta no está activa.");
            return;
        }


        if (!cuenta.tieneVehiculoConDominio(dominio)) {
            System.out.println("Error: El dominio no corresponde al vehículo registrado en la cuenta.");
            return;
        }

        Lugar lugarLibre = null;
        for (Lugar lugar : Estacionamiento.getLugares()) {
            if (lugar.getEstado().getNombre().equals("Libre")) {
                lugarLibre = lugar;
                break;
            }
        }

        if (lugarLibre == null) {
            System.out.println("No hay lugares disponibles.");
            return;
        }

        
        lugarLibre.setEstado(new Estado("Ocupado"));
        this.lugar = lugarLibre;

        
        this.fechaYhoraIngreso = LocalDateTime.now();
        this.dominio = dominio;
        this.cuenta = cuenta;

        //no necesariamente se tiene una cuenta
        //crear otro método

        TipoUsuario tipoUsuarioCuenta = cuenta.getTipoUsuario();
        if (tipoUsuarioCuenta.getNombre().equals("Invitado")) {
            this.esInvitado = true;
        } else {
            this.esInvitado = false;
        }

        if (tipoUsuarioCuenta.getNombre().equals("Externo")) {
            this.esExterno = true;
        } else {
            this.esExterno = false;
        }

        if (this.esInvitado){
            System.out.println("Ingreso registrado como invitado. No se realiza cobro.");
            return;
        }

        //no hace falta
        TipoUsuario tiposUsuariosCuenta = tiposUsuarios.stream()
            .filter(tu -> tu.getNombre().equalsIgnoreCase(tipoUsuarioCuenta.getNombre()))
            .findFirst()
            .orElse(null);

        if (tiposUsuariosCuenta == null) {
            System.out.println("Tipo de usuario no reconocido.");
            return;
        }

        System.out.println("Ingreso registrado correctamente.");
        if (this.esInvitado) {
            System.out.println("Tipo de usuario: Invitado. No se realizará cobro.");
        } else if (this.esExterno) {
            System.out.println("Tipo de usuario: Externo. El cobro se calculará al egresar.");
        } else {
            System.out.println("Tipo de usuario: Frecuente. Se evaluará abono o saldo al egresar.");
        }

    }

}
