package api;

public class EndPoints {
    public final static String BASE_URL = "http://localhost";
    public final static int PORT = 8000;

    public final static String car = "/car";
    public final static String carList = "/car_list";
    public final static String updateCar = "/car/update/{model}";
    public final static String deleteCar = "/car/{model}";
}
