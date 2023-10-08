import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {

    private static final Map<String, TipoToken> palabrasReservadas;
    private static final Map<String, TipoToken> caracterUnico;

    static {
        palabrasReservadas = new HashMap<>();
        palabrasReservadas.put("and", TipoToken.AND);
        palabrasReservadas.put("else", TipoToken.ELSE);
        palabrasReservadas.put("false", TipoToken.FALSE);
        palabrasReservadas.put("for", TipoToken.FOR);
        palabrasReservadas.put("fun", TipoToken.FUN);
        palabrasReservadas.put("if", TipoToken.IF);
        palabrasReservadas.put("null", TipoToken.NULL);
        palabrasReservadas.put("or", TipoToken.OR);
        palabrasReservadas.put("print", TipoToken.PRINT);
        palabrasReservadas.put("return", TipoToken.RETURN);
        palabrasReservadas.put("true", TipoToken.TRUE);
        palabrasReservadas.put("var", TipoToken.VAR);
        palabrasReservadas.put("while", TipoToken.WHILE);

        // Creamos otro hashmap que servirá para símbolos de un solo caracter
        caracterUnico = new HashMap<>();
        caracterUnico.put("(", TipoToken.LEFT_PAREN);
        caracterUnico.put(")", TipoToken.RIGHT_PAREN);
        caracterUnico.put("{", TipoToken.LEFT_BRACE);
        caracterUnico.put("}", TipoToken.RIGHT_BRACE);
        caracterUnico.put(".", TipoToken.DOT);
        caracterUnico.put(",", TipoToken.COMMA);
        caracterUnico.put(";", TipoToken.SEMICOLON);
        caracterUnico.put("*", TipoToken.STAR);
        caracterUnico.put("+", TipoToken.PLUS);
        caracterUnico.put("-", TipoToken.MINUS);
    }

    private final String source;

    private final List<Token> tokens = new ArrayList<>();

    public Scanner(String source) {
        this.source = source + " ";
    }

    // Definimos una lista para los símbolos de un solo caracter
    List<String> simbolos = Arrays.asList(
            "(", ")", "{", "}", ".", ",", ";", "*", "+", "-");

    public List<Token> scan() throws Exception {
        String lexema = "";
        int estado = 0;
        char c;

        // Consideramos estado 0 como inicial y estado -1 como final

        for(int i=0; i<source.length(); i++) {

            c = source.charAt(i);

            switch(estado){

                // Definimos los estados del autómata

                // Definimos 0 como estado inicial
                case 0:

                    if (Character.isLetter(c)) {

                        estado = 1;
                        lexema += c;
                    }

                    else if(Character.isDigit(c)){

                        estado = 2;
                        lexema += c;
                    }

                    else if (c == '/') {

                        estado = 10;
                        lexema += c;
                    }

                    else if (simbolos.contains(c + "")) {

                        estado = 14;
                        lexema += c;
                    }

                break;

                case 2:

                    if(Character.isDigit(c)){
                        
                        estado = 2;
                        lexema += c; 
                    }

                    else if(c == '.'){

                        estado = 3;
                        lexema += c;
                    }

                    else if(c == 'E'){

                        estado = 5;
                        lexema += c;
                    }

                    else{

                        // Creamos token para número entero
                        Token new_token = new Token(TipoToken.NUMBER, lexema, Integer.valueOf(lexema));
                        tokens.add(new_token);

                        estado = 0;
                        lexema = "";
                        i--;

                    }

                break;

                case 3:

                    if(Character.isDigit(c)){

                        estado = 4;
                        lexema += c;
                    
                    }else{
                        
                        estado = -1;
                        lexema += c;
                    }

                break;

                case 4:

                    if(Character.isDigit(c)){

                        estado = 4;
                        lexema += c;
                    }

                    else if(c == 'E'){

                        estado = 5;
                        lexema += c;
                    }

                    else{

                        // Creamos token para número con decimal
                        Token new_token = new Token(TipoToken.NUMBER, lexema, Double.valueOf(lexema));
                        tokens.add(new_token);

                        estado = 0;
                        lexema = "";
                        i--;

                    }


                break;


                case 5:
                    
                if(c == '+' || c == '-'){

                    estado = 6;
                    lexema += c;
                
                }

                else if(Character.isDigit(c)){

                    estado = 7;
                    lexema += c;
                }

                else{

                    estado = -1;
                    lexema += c;

                }

                break;

                case 6:

                    if(Character.isDigit(c)){

                        estado = 7;
                        lexema += c;
                    
                    }

                    else{

                        estado = -1;
                        lexema += c;
                    }

                break;

                case 7:

                    if(Character.isDigit(c)){

                        estado = 7;
                        lexema += c;
                    }

                    else{

                        // Creamos token para número con exponente
                        Token new_token = new Token(TipoToken.NUMBER, lexema, Double.valueOf(lexema));
                        tokens.add(new_token);

                        estado = 0;
                        lexema = "";
                        i--;
                    }

                break;
                


                case 10:

                    if (c == '*') {

                        estado = 12;
                        lexema += c;
                    }

                    else if (c == '/') {

                        estado = 11;
                        lexema += c;
                    }

                    else {

                        // Creamos token para símbolo de división
                        Token new_token = new Token(TipoToken.SLASH, lexema);
                        tokens.add(new_token);

                        estado = 0;
                        lexema = "";
                        i--;

                    }

                break;

                case 11:

                    if (c != '\n' && c != '\r') {

                        estado = 11;
                        lexema += c;
                    }

                    else {

                        // No creamos token para comentario, regresamos cadena a vacía y estado a 0
                        estado = 0;
                        lexema = "";

                    }

                break;

                case 12:

                    if (c == '*') {

                        estado = 13;
                        lexema += c;

                    } else {

                        estado = 12;
                        lexema += c;
                    }

                break;

                case 13:

                    if (c == '\n') {

                        estado = 12;
                        lexema += c;
                    }

                    else if (c == '/') {

                        // no creamos token para comentario, regresamos a cadena vacia y al estado 0
                        lexema += c;

                        estado = 0;
                        lexema = "";
                    }

                    else {

                        estado = 13;
                        lexema += c;
                    }

                break;

                case 14:

                    // Creamos el token para caracteres de un solo símbolo
                    TipoToken t_token = caracterUnico.get(lexema);
                    Token new_Token = new Token(t_token, lexema);

                    tokens.add(new_Token);

                    estado = 0;
                    lexema = "";
                    i--;

                break;
            }
        }

        return tokens;
    }
}