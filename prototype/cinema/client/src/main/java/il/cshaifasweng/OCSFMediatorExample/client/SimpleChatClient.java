package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * JavaFX App
 */
public class SimpleChatClient extends Application {

    private static Scene scene;
    private SimpleClient client;
    private static Stage appStage;
    private static Map<List<String>,List<Integer>> rooms = new HashMap<List<String>,List<Integer>>();

    @Override
    public void start(Stage stage) throws IOException {
    	EventBus.getDefault().register(this);
        create_rooms();
        //print_rooms();

        stage.setOnCloseRequest(event -> {
            Message message = new Message(10101, "#Log_out_all");

            if(UserLogInWithIDController.idUser != null ) {
                message.setObject(UserLogInWithIDController.idUser);
                message.setMessage("#Log_out_user");
            }
            else if (WorkerLogInController.worker != null) {
                message.setObject(WorkerLogInController.worker);
                message.setMessage("#Log_out_worker");
            }
            else{return;}
            try {
                SimpleClient.getClient().sendToServer(message);
            } catch (IOException e) {
                System.err.println("Error sending message to server");
                return;
            }
        });

        scene = new Scene(loadFXML("host"), 640, 480);
        appStage = stage;
        stage.setScene(scene);
        stage.show();
    }

    public static void setWindowTitle (String title) {
        appStage.setTitle(title);
    }
    public static String getWindowTitle() {
        return appStage.getTitle();
    }

    public static void setRoot(String pageName) throws IOException {
        if (pageName == null || pageName.equals("")) {
            Parent root = loadFXML("MasterPage");
            scene = new Scene(root);
            appStage.setScene(scene);
            appStage.show();
        }
        else {
            EventBus.getDefault().post(new BeginContentChangeEnent(pageName));
        }
    }

    /*static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }*/

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SimpleChatClient.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    


    @Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
    	EventBus.getDefault().unregister(this);
		super.stop();
	}


    @Subscribe
    public void onMessageEvent(BaseEventBox message) {
        if(message.getId()==13) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            Platform.runLater(() -> {
                Alert alert = new Alert(AlertType.INFORMATION,
                        String.format("Message:\nId: %d\nData: %s\nTimestamp: %s\n",
                                message.getMessage().getId(),
                                message.getMessage().getMessage(),
                                message.getMessage().getTimeStamp().format(dtf))
                );
                alert.setTitle("new message");
                alert.setHeaderText("New Message:");
                alert.show();
            });
        }
    }


	public static void main(String[] args) {
        launch();
    }
    public static void create_rooms()
    {
        rooms = new HashMap<List<String>,List<Integer>>();
        rooms.put(Arrays.asList("Sakhnin","1"),Arrays.asList(10,10));
        rooms.put(Arrays.asList("Sakhnin","2"),Arrays.asList(5,10));
        rooms.put(Arrays.asList("Sakhnin","3"),Arrays.asList(7,7));
        rooms.put(Arrays.asList("Haifa","1"),Arrays.asList(10,10));
        rooms.put(Arrays.asList("Haifa","2"),Arrays.asList(5,10));
        rooms.put(Arrays.asList("Haifa","3"),Arrays.asList(7,7));
        rooms.put(Arrays.asList("Nazareth","1"),Arrays.asList(10,10));
        rooms.put(Arrays.asList("Nazareth","2"),Arrays.asList(5,10));
        rooms.put(Arrays.asList("Nazareth","3"),Arrays.asList(7,7));
        rooms.put(Arrays.asList("Nhif","1"),Arrays.asList(10,10));
        rooms.put(Arrays.asList("Nhif","2"),Arrays.asList(5,10));
        rooms.put(Arrays.asList("Nhif","3"),Arrays.asList(7,7));


    }
    public static ArrayList<String> get_branches()
    {
        ArrayList<String> categories = new ArrayList<>();
        categories.add("Sakhnin");
        categories.add("Haifa");
        categories.add("Nazareth");
        categories.add("Nhif");
        return categories;
    }
    public static void print_rooms()
    {
        for (Map.Entry<List<String>, List<Integer>> entry : rooms.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }
    }
    public static List<Integer> get_rows_and_columns(List<String> keys) {
        return rooms.get(keys);
    }
    private static void add_to_rooms(String Branch,int room_number,int row_size,int column_size) {
        ArrayList<String> keys = new ArrayList<>();
        keys.add(Branch);
        keys.add(String.valueOf(room_number));
        ArrayList<Integer>values = new ArrayList<Integer>();
        values.add(row_size);
        values.add(column_size);
        rooms.put(keys,values);
    }
    public static List<String> get_categories() {
        ArrayList<String> categories = new ArrayList<>();
        categories.add("Comedy");
        categories.add("Sci-Fi");
        categories.add("Action");
        categories.add("Romance");
        categories.add("Family");
        return categories;
    }

}