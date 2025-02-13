
public class Estudiante {
    int matricula, semestre, calificacion;
    String nombre, carrera, apellido;

    public Estudiante(String carrera, int matricula, int semestre, String apellido, String nombre, int calificacion) {
        this.carrera = carrera;
        this.matricula = matricula;
        this.semestre = semestre;
        this.apellido = apellido;
        this.nombre = nombre;
        this.calificacion = calificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    @Override
    public String toString() {
        return "\nmatricula=" + matricula + ", semestre=" + semestre + ", calificacion=" + calificacion + ", nombre=" + nombre + ", carrera=" + carrera + ", apellido=" + apellido + '}';
    }
        public boolean matches(String campo) {
    if (campo.isEmpty()) {
        return false;
    }
    
    String campoLowerCase = campo.toLowerCase();
    if (carrera.toLowerCase().contains(campoLowerCase)) {
        return true;
    }
    if (String.valueOf(semestre).contains(campo)) {
        return true;
    }
    if (apellido.toLowerCase().contains(campoLowerCase)) {
        return true;
    }
    if (nombre.toLowerCase().contains(campoLowerCase)) {
        return true;
    }
    if (String.valueOf(calificacion).contains(campo)) {
        return true;
    } 
    return false;
}
}
