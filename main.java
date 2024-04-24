
class JDBC {

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("CONNECTED SUCCCESSFULLY ");
        } catch (Exception c) {
            c.getStackTrace();
        }

    }

}

class main {
    public static void main(String args[]) {
        JDBC obj = new JDBC();
        obj.connect();
        new Expenses();
    }
}
