package main.restaurant_app;

public class restaurantPasswordHelper {
    private StringBuilder display;

    public restaurantPasswordHelper() {
        setDisplay("");
    }

    public String addKey(String key) {
        switch (key.toLowerCase()) {
            case "enter":
                System.out.println(display.length());
                if (display.length() != 0) {
                    int code = Integer.parseInt(String.valueOf(display));
                    System.out.println(code);
                }
                setDisplay("");
                // ENTER TO SPECIFIC USER'S PAGE HERE
                break;
            case "backspace":
                if (display.length() > 0) {
                    display.deleteCharAt(display.length()-1);
                }
                break;
            default:
                display.append(key);
                break;

        }

        return display.toString();
    }

    private void setDisplay(String init) {
        init = (init == null) ? "" : init;
        display = new StringBuilder(init);
    }
}
