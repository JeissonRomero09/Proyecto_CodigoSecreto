	package co.edu.poli.CodigoOculto.Modelo;
	
	public class Validacion {
	
	    public static String[] validar(int[] intento, int[] combinacion) {
	        int n = combinacion.length;
	
	        String[] resultado = new String[n];
	        boolean[] usado = new boolean[n];
	
			// Inicializar
			for (int i = 0; i < n; i++) {
				resultado[i] = "GRIS";
				usado[i] = false;
			}
	
	        // VERDES (posición correcta)
	        for (int i = 0; i < n; i++) {
	            if (intento[i] == combinacion[i]) {
	                resultado[i] = "VERDE";
	                usado[i] = true;
	            }
	        }
	
	        // AMARILLOS (existe pero en otra posición)
	        for (int i = 0; i < n; i++) {
	            if (!resultado[i].equals("VERDE")) {
	                for (int j = 0; j < n; j++) {
	                    if (!usado[j] && intento[i] == combinacion[j]) {
	                        resultado[i] = "AMARILLO";
	                        usado[j] = true;
	                        break;
	                    }
	                }
	            }
	        }
	
	        return resultado;
	    }
	}