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

        for (int i = 0; i < source.length(); i++) {
            c = source.charAt(i);

            switch (estado) {

                // Definimos los estados del autómata

                // Definimos 0 como estado inicial
                case 0:

                    if (Character.isLetter(c)) {

                        estado = 1;
                        lexema += c;
                    }

                    else if (simbolos.contains(c + "")) {

                        estado = 14;
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
