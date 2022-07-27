package my.id.raditsan.myemployee.utils;

public class Response {
    private int statusCode;
    private String result;

    public Response(int statusCode, String result) {
        this.statusCode = statusCode;
        this.result = result;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
