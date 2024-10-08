package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.IdUser;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Worker;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.event.ActionEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import javafx.stage.Screen;

import java.io.IOException;

public class MasterPageCotroller {

    @FXML
    private Menu Defalult_menu;

    @FXML
    private Menu Sing_in_menu;

    @FXML
    private BorderPane border_pane;

    @FXML
    private StackPane content_area;

    @FXML
    private Menu customer_service_menu;

    @FXML
    private Menu data_manger_menu;

    @FXML
    private Menu manger_menu;

    @FXML
    private Menu sing_out_menu;

    @FXML
    private Menu user_menu;

    @FXML
    private Menu branch_manger_menu;

    @Subscribe
    public void change_content(ContentChangeEvent event)
    {
        Platform.runLater(()->{
            System.out.println(event.getPage() + "1");
            setContent(event.getPage()+".fxml");
            System.out.println(event.getPage() + "2");
            create_activity_list();
            System.out.println(event.getPage() + "3");

        });

        /*Platform.runLater(()->{
            javafx.geometry.Rectangle2D screenBounds = Screen.getPrimary().getBounds();
            double screenWidth = screenBounds.getWidth();
            double screenHeight = screenBounds.getHeight();
            content_area.layoutXProperty().setValue(screenWidth/2);
        });*/
    }

    @Subscribe
    public void onUpdateIdUserEvent(BaseEventBox event) {
        if(event.getId() == BaseEventBox.get_event_id("GET_USER_WORKER_NAME")) {
            Platform.runLater(()->{
                
                if(UserLogInWithIDController.idUser != null) {
                    sing_out_menu.setText("Hello "+ UserLogInWithIDController.idUser.getName());
                }
                else if(WorkerLogInController.worker != null) {
                    sing_out_menu.setText("Hello "+ WorkerLogInController.worker.getUser_name());
                }
            });
        }
    }

    private void create_activity_list(){
        Defalult_menu.setVisible(false);
        customer_service_menu.setVisible(false);
        data_manger_menu.setVisible(false);
        manger_menu.setVisible(false);
        user_menu.setVisible(false);
        Sing_in_menu.setVisible(false);
        sing_out_menu.setVisible(false);
        branch_manger_menu.setVisible(false);
        IdUser user = UserLogInWithIDController.idUser;
        Worker worker = WorkerLogInController.worker;
        if (user == null && worker == null) {
            Defalult_menu.setVisible(true);
            Sing_in_menu.setVisible(true);
        }
        if (user != null) {
            Defalult_menu.setVisible(true);
            user_menu.setVisible(true);
            sing_out_menu.setVisible(true);
        }
        if (worker != null) {
            Defalult_menu.setVisible(true);
            sing_out_menu.setVisible(true);
            if(worker.getRole().equals("Manager") || worker.getRole().equals("manager"))
            {
                manger_menu.setVisible(true);
            }
            if (worker.getRole().equals("DataManager"))
            {
                data_manger_menu.setVisible(true);
            }
            if (worker.getRole().equals("CustomerService"))
            {
                customer_service_menu.setVisible(true);
            }
            if(worker.getRole().equals("branchManager"))
            {
                branch_manger_menu.setVisible(true);
            }
        }

    }


    @FXML
    public void initialize() {

        EventBus.getDefault().register(this);

        // Initialize common UI components and behavior here
        // catalog_menu.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> setContent("Movie_editing_details.fxml"));
        create_activity_list();
        setContent("HomePage.fxml");
        Menu menu = new Menu("File");

    }

    public void setContent(String file) {
        FXMLLoader new_content = null;
        try {
            new_content = new FXMLLoader(getClass().getResource(file));


        } catch (Exception e) {

            e.printStackTrace();
            return;
        }

        content_area.getChildren().clear();
        try {
            content_area.getChildren().setAll((Node) new_content.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void handle_action(ActionEvent event) {
        MenuItem source = (MenuItem) event.getSource();
        String menuItemText = source.getText();
        if (menuItemText.equals("Home page")) {
            EventBus.getDefault().post(new BeginContentChangeEnent("HomePage"));
            //EventBus.getDefault().post(new BeginContentChangeEnent("TheaterMap"));
        }
        else if(menuItemText.equals("id")){
            EventBus.getDefault().post(new BeginContentChangeEnent("UserLogInWithID"));
        }
        else if (menuItemText.equals("worker"))
        {
            EventBus.getDefault().post(new BeginContentChangeEnent("WorkerLogIn"));
        } else if (menuItemText.equals("Purchases")) {
            EventBus.getDefault().post(new BeginContentChangeEnent("UserPurchases"));
        }
        else if(menuItemText.equals("Complains"))
        {
            EventBus.getDefault().post(new BeginContentChangeEnent("UserComplains"));
        }

        else if (menuItemText.equals("Purchase Multi Entry Ticket")) {
            EventBus.getDefault().post(new BeginContentChangeEnent("MultiEntryTicket"));
        }

        else if (menuItemText.equals("Sign out")) {
            Message m = null ;
            if (UserLogInWithIDController.idUser != null) {
                m = new Message(30, "#SignOut_UserID");
                m.setObject(UserLogInWithIDController.idUser);
            }
            else {
                m = new Message(30, "#SignOut_Worker");
                m.setObject(WorkerLogInController.worker);
            }
            try {
                SimpleClient.getClient().sendToServer(m);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            UserLogInWithIDController.idUser = null;
            WorkerLogInController.worker = null;
            create_activity_list();
            EventBus.getDefault().post(new BeginContentChangeEnent("HomePage"));
        }
        else if (menuItemText.equals("MovieEditDetails")) {
            EventBus.getDefault().post(new BeginContentChangeEnent("Movie_editing_details"));
        } else if (menuItemText.equals("Catalog")) {

            EventBus.getDefault().post(new BeginContentChangeEnent("Catalog"));

        }
        else if (menuItemText.equals("handle complains")) {
            EventBus.getDefault().post(new BeginContentChangeEnent("CustomerService"));
        }
        else if (menuItemText.equals("Reports")) {
            EventBus.getDefault().post(new BeginContentChangeEnent("Reports"));
        }
        else if (menuItemText.equals("Edited Prices")) {
            EventBus.getDefault().post(new BeginContentChangeEnent("ChangesManager"));
        }
    }

}