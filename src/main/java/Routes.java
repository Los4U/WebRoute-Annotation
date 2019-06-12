public class Routes {

    @WebRoute("/test1")
    public String test1(String met){

        if("POST".equals(met)){
            return "POST 111";
        }else{
            return "GET 111";
        }

    }

    @WebRoute("/test2")
    public String test2(String met){

        if("POST".equals(met)){
            return "POST 22222";
        }else{
            return "GET 22222";
        }

    }
}
