import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Principal {


    static ArrayList<Estudiante> al=new ArrayList<Estudiante>();
    public static void main(String[] args) {
        
        String carrera;
        int matricula;
        int semestre;
        String apellido;
        String nombre;
        int calificion;
       BufferedReader br = null;
         try
         {            
           br= new BufferedReader(new FileReader(new File("estudiantesOBJ.csv")));
           String linea= br.readLine();
           while(linea!=null)
           {
               try{ 
                   String datos[] = linea.split(",");
                   carrera=datos[0];
                   matricula=Integer.parseInt(datos[1]);
                   semestre = Integer.parseInt(datos[2]);
                   apellido=datos[3];
                   nombre = datos [4];
                   calificion=Integer.parseInt(datos[5]);
                   al.add(new Estudiante(carrera,matricula,semestre,apellido,nombre,calificion));
               }
               catch(Exception e){
                    
                }              
              linea= br.readLine();   
           }            
         }
         catch(Exception e)
         {
          System.out.println( e.getMessage());
         }
int o =-1;
        while (o!=0){
            o = Integer.parseInt(JOptionPane.showInputDialog("Menu \n"
            +"0.- Salir \n"
            +"1.- Buscar diferentes campos\n"
            +"2.- Buscar por prefijo \n"
            +"3.- Buscar palabra completa \n"
            +"4.- Buscar por sufijo \n"));
            switch(o){
                case 1:{
                   String campo = JOptionPane.showInputDialog("Ingrese un numero a letra a buscar:");
                    ArrayList<Estudiante> resultadosCampo = buscarPorCampo(campo);
                    if (resultadosCampo.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No se encontraron estudiantes para el campo especificado.");
                    } else {
                        mostrarEstudiantes(resultadosCampo);
                    }
                    break;
                }
                case 2: {
                    int subOption = -1;
                        while (subOption != 0) {
                    subOption = Integer.parseInt(JOptionPane.showInputDialog("Submenú\n"
                + "1. Buscar por nombre\n"
                + "2. Buscar por apellido\n"
                + "0. Regresar"));

                 switch (subOption) {
            case 1:
                nombre = JOptionPane.showInputDialog("Ingrese el nombre a buscar:");
                buscarNombre(nombre);
                break;
            case 2: 
                apellido = JOptionPane.showInputDialog("Ingrese el apellido a buscar:");
                buscarApellido(apellido);
                break;
            case 0:
                break;
            default:
                JOptionPane.showMessageDialog(null, "Opción inválida");
                break;
              }
             }
                    break;
                }
                case 3: {
                   String campo = JOptionPane.showInputDialog("Ingrese nombre o apellido completo buscar:");
                    ArrayList<Estudiante> resultadosCampo = buscarPorCompleto(campo);
                    if (resultadosCampo.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No se encontraron estudiantes para el campo especificado.");
                    } else {
                        mostrarEstudiantes(resultadosCampo);
                    }
                    break;
                }
                case 4: {
                    int subOption = -1;
                        while (subOption != 0) {
                    subOption = Integer.parseInt(JOptionPane.showInputDialog("Submenú\n"
                + "1. Buscar por nombre\n"
                + "2. Buscar por apellido\n"
                + "0. Regresar"));

                 switch (subOption) {
            case 1:
                nombre = JOptionPane.showInputDialog("Ingrese el nombre a buscar:");
                buscarNombre2(nombre);
                break;
            case 2: 
                apellido = JOptionPane.showInputDialog("Ingrese el apellido a buscar:");
                buscarApellido2(apellido);
                break;
            case 0:
                break;
            default:
                JOptionPane.showMessageDialog(null, "Opción inválida");
                break;
              }
             }
                    break;
                }
            }
        }
    } 
     private static ArrayList<Estudiante> buscarPorCampo(String campo) {
        ArrayList<Estudiante> resultados = new ArrayList<>();
        for (Estudiante estudiante : al) {
            if (estudiante.matches(campo)) {
                resultados.add(estudiante);
            }
        }
        return resultados;
     }
private static void mostrarEstudiantes(ArrayList<Estudiante> estudiantes) {
    if (estudiantes.isEmpty()) {
        System.out.println("No se encontraron estudiantes.");
    } else {
        System.out.println("Estudiantes encontrados:");
        for (Estudiante estudiante : estudiantes) {
            System.out.println(estudiante);
        }
    }
}
private static ArrayList<Estudiante> buscarPorCompleto(String campo) {
    ArrayList<Estudiante> resultados = new ArrayList<>();
    
    for (Estudiante estudiante : al) {
        if (estudiante.getNombre().equalsIgnoreCase(campo) || estudiante.getApellido().equalsIgnoreCase(campo)) {
            resultados.add(estudiante);
        }
    }
    
    return resultados;
}
public static void buscarApellido(String a) {
    int izq = 0;
    int der = al.size() - 1;
    int centro = 0;
    while (izq <= der) {
        centro = izq + (der - izq) / 2;
        if (al.get(centro).getApellido().matches("(?i).*"+ a + ".*")) {
            System.out.println("Encontrado Alumno: " + al.get(centro).toString());
            break;
        }
        if (al.get(centro).getApellido().compareTo(a) < 0) {
            izq = centro + 1;
        } else {
            der = centro - 1;
        }
    }
}
public static void buscarNombre(String a) {
    int izq = 0;
    int der = al.size() - 1;
    int centro = 0;
    while (izq <= der) {
        centro = izq + (der - izq) / 2;
        if (al.get(centro).getNombre().matches("(?i).*"+ a + ".*")) {
            System.out.println("Encontrado Alumno: " + al.get(centro).toString());
            break;
        }
        if (al.get(centro).getNombre().compareTo(a) < 0) {
            izq = centro + 1;
        } else {
            der = centro - 1;
        }
    }
}
public static void buscarNombre2(String a) {
    int izq = 0;
    int der = al.size() - 1;
    int centro = 0;
    while (izq <= der) {
        centro = izq + (der - izq) / 2;
        if (al.get(centro).getNombre().matches(".*"+a )) {
            System.out.println("Encontrado Alumno: " + al.get(centro).toString());
            break;
        }
        if (al.get(centro).getNombre().compareTo(a) < 0) {
            izq = centro + 1;
        } else {
            der = centro - 1;
        }
    }
}
public static void buscarApellido2(String a) {
int izq = 0;
    int der = al.size() - 1;
    int centro = 0;
    while (izq <= der) {
        centro = izq + (der - izq) / 2;
        if (al.get(centro).getApellido().matches(".*"+ a)) {
            System.out.println("Encontrado Alumno: " + al.get(centro).toString());
            break;
        }
        if (al.get(centro).getApellido().compareTo(a) < 0) {
            izq = centro + 1;
        } else {
            der = centro - 1;
        }
}
}
}