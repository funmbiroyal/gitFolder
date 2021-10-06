package exceptions;

public class SchoolException extends RuntimeException{
    public SchoolException(){
        super();

    }
    public SchoolException(String message){
        super(message);

    }
    public SchoolException(String message,Throwable ex){
        super(message,ex);

    }
    public SchoolException(Throwable ex){
        super(ex);

    }
}
