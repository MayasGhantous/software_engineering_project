package il.cshaifasweng.OCSFMediatorExample.client;

import static il.cshaifasweng.OCSFMediatorExample.client.MovieEditingDetailsController.go_to_screening_movie;

import com.mysql.cj.protocol.x.XMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.Screening;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.fxml.Initializable;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DateStringConverter;
import javafx.scene.input.MouseEvent;


import javax.persistence.criteria.CriteriaBuilder;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.net.URL;
import java.sql.Time;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static il.cshaifasweng.OCSFMediatorExample.client.MovieEditingDetailsController.go_to_screening_movie;
import static il.cshaifasweng.OCSFMediatorExample.client.SimpleClient.Current_Message;
import static il.cshaifasweng.OCSFMediatorExample.client.SimpleClient.getClient;


public class EditScreeningController {


    @FXML
    private ComboBox<String> Branch;

    @FXML
    private Text ErrorMessage;

    @FXML
    private Text Movie_name;

    @FXML
    private TextField Screening_ID;

    @FXML
    private Button add;

    @FXML
    private TableColumn<Screening,String> branch_column;

    @FXML
    private TextField date;

    @FXML
    private TableColumn<Screening,Date> date_column;

    @FXML
    private TableColumn<Screening,Integer> id_column;

    @FXML
    private Button remove;

    @FXML
    private TableColumn<Screening,Integer> room_column;

    @FXML
    private TextField room_number;

    @FXML
    private TextField screening_time;

    @FXML
    private TextField rows_number;

    @FXML
    private TextField column_number;

    @FXML
    private TableColumn<Screening,Date> screening_time_column;

    @FXML
    private ComboBox<String> search_branch_combobox;

    @FXML
    private TableView<Screening> table_view;

    @FXML
    private TextArea theater_map;

    @FXML
    private Button update;

    public void search_branch_function() {
        Message message = new Message(8,"#SearchBranchForScreening");
        message.setObject(current_movie);
        if(search_branch_combobox.getValue() != null)
            message.setObject2(search_branch_combobox.getValue().toString());
        else
            message.setObject2("");

        try {
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void remove_screening(ActionEvent event) {
        ErrorMessage.setVisible(false);
        if(Screening_ID.getText().trim().isEmpty())
        {
            ErrorMessage.setVisible(true);
            ErrorMessage.setText("select a screening");
            return;
        }
        Message message = new Message(10,"#RemoveScreening");
        message.setObject(current_screening);

        try {
            SimpleClient.getClient().sendToServer(message);
            Screening_ID.setText("");
            rows_number.setText("");
            column_number.setText("");
            screening_time.setText("");
            theater_map.setText("");
            date.setText("");
            Branch.setValue("");
            room_number.setText("");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void search(ActionEvent event) {
        ErrorMessage.setVisible(false);
        search_branch_function();
    }

    @FXML
    void update_screning(ActionEvent event) {
        ErrorMessage.setVisible(false);
        boolean we_need_to_add_5 = true;
        if(date.getText().trim().endsWith("a"))
        {
            date.setText(date.getText().replace("a",""));
            we_need_to_add_5 = false;
        }
        if(Screening_ID.getText().trim().isEmpty())
        {
            ErrorMessage.setVisible(true);
            ErrorMessage.setText("select a screening");
            return;
        }
        if(date.getText().trim().isEmpty())
        {
            ErrorMessage.setVisible(true);
            ErrorMessage.setText("enter a date");
            return;
        }
        if(screening_time.getText().trim().isEmpty())
        {
            ErrorMessage.setVisible(true);
            ErrorMessage.setText("enter a time");
            return;
        }

        String dateC = date.getText();
        String timeC = screening_time.getText();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date real_date = null;
        try {
             real_date = formatter.parse(dateC+" "+timeC);
        } catch (ParseException e) {
            ErrorMessage.setVisible(true);
            ErrorMessage.setText("date format: dd/MM/yyyy, time format: HH:mm");
            return;
        }
        String date_reg  ="^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";
        Pattern pattern = Pattern.compile(date_reg);
        // Match the input string (date) against the pattern
        Matcher matcher = pattern.matcher(dateC);
        // Return true if the date matches the pattern, otherwise false
        if (!matcher.matches())
        {
            ErrorMessage.setVisible(true);
            ErrorMessage.setText("this is not a valid date");
            return;
        }

        String time_reg = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$";
         pattern = Pattern.compile(time_reg);
        // Match the input string (date) against the pattern
         matcher = pattern.matcher(timeC);
        // Return true if the date matches the pattern, otherwise false
        if (!matcher.matches())
        {
            ErrorMessage.setVisible(true);
            ErrorMessage.setText("this is not a valid time");
            return;
        }

        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        if (we_need_to_add_5) {
            cal.add(Calendar.HOUR_OF_DAY, 5);
        }
        Date after_5 = cal.getTime();
        if(real_date.before(after_5))
        {
            ErrorMessage.setVisible(true);
            if (we_need_to_add_5) {
                ErrorMessage.setText("screening must be at least 5 hours from now");
            }
            else {
                ErrorMessage.setText("screening cant be in the past");
            }
            return;
        }
        current_screening.setDate_time(real_date);
        Message message = new Message(10,"#UpdateScreening");
        message.setObject(current_screening);


        try {
            SimpleClient.getClient().sendToServer(message);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @FXML
    void add_screening(ActionEvent event) {
        ErrorMessage.setVisible(false);
        boolean we_need_to_add_5 = true;
        if(date.getText().trim().endsWith("a"))
        {
            date.setText(date.getText().replace("a",""));
            we_need_to_add_5 = false;
        }
        if(Branch.getValue() == null || Branch.getValue().isEmpty())
        {
            ErrorMessage.setVisible(true);
            ErrorMessage.setText("Please select a Branch");
            return;
        }
        if(room_number.getText().trim().isEmpty())
        {
            ErrorMessage.setVisible(true);
            ErrorMessage.setText("Please enter a Room Number");
            return;
        }
        if (rows_number.getText().trim().isEmpty())
        {
            ErrorMessage.setVisible(true);
            ErrorMessage.setText("this branch does not have this room number");
            return;
        }
        if(screening_time.getText().trim().isEmpty()){
            ErrorMessage.setVisible(true);
            ErrorMessage.setText("Please enter a Screening Time");
            return;
        }
        if (date.getText().trim().isEmpty()){
            ErrorMessage.setVisible(true);
            ErrorMessage.setText("Please enter a Date");
            return;

        }
        String branch = Branch.getValue();
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Date datec = null;
        try {
             datec = targetFormat.parse(date.getText());
        } catch (ParseException e) {
            ErrorMessage.setVisible(true);
            ErrorMessage.setText("date format dd/MM/yyyy");
            return;

        }
        Date time1 = null;
        try {
            time1 = timeFormat.parse(screening_time.getText());
        } catch (ParseException e) {
            ErrorMessage.setVisible(true);
            ErrorMessage.setText("screening time format HH:mm");
            return;

        }

        String date_reg  ="^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";
        Pattern pattern = Pattern.compile(date_reg);
        // Match the input string (date) against the pattern
        Matcher matcher = pattern.matcher(date.getText());
        // Return true if the date matches the pattern, otherwise false
        if (!matcher.matches())
        {
            ErrorMessage.setVisible(true);
            ErrorMessage.setText("this is not a valid date");
            return;
        }

        String time_reg = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$";
        pattern = Pattern.compile(time_reg);
        // Match the input string (date) against the pattern
        matcher = pattern.matcher(screening_time.getText().trim());
        // Return true if the date matches the pattern, otherwise false
        if (!matcher.matches())
        {
            ErrorMessage.setVisible(true);
            ErrorMessage.setText("this is not a valid time");
            return;
        }

        try {
            datec = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(date.getText().trim()+" "+screening_time.getText().trim());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        if (we_need_to_add_5) {
            cal.add(Calendar.HOUR_OF_DAY, 5);
        }
        Date after_5 = cal.getTime();
        if(datec.before(after_5))
        {
            ErrorMessage.setVisible(true);
            if (we_need_to_add_5) {
                ErrorMessage.setText("screening must be at least 5 hours from now");
            }
            else {
                ErrorMessage.setText("screening cant be in the past");
            }
            return;
        }

        int room = Integer.parseInt(room_number.getText());
        String theater = "";
        int rowc = Integer.parseInt(rows_number.getText());
        int columnc = Integer.parseInt(column_number.getText());
        for(int i=0 ; i<rowc;i++) {
            for (int j = 0; j < columnc-1; j++) {

                if(i==2&j==3)
                {
                    //theater+="-1, ";
                }
                else {
                    theater+="0, ";
                }
            }

            theater+="0\n";
        }
        Screening screening = new Screening(datec,room,theater,branch);
        screening.setMovie(current_movie);

        Message message = new Message(4,"#AddNewScreening");
        message.setObject(screening);


        try {
            SimpleClient.getClient().sendToServer(message);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Subscribe
    public void update_each_user(BaseEventBox event){
        if(event.getId()==7) {
            Platform.runLater(() -> {
                get_data(event.getMessage());
            });
        }
        else if(event.getId() == BaseEventBox.get_event_id("THEATER_MAP_UPDATED"))
        {
            Platform.runLater(()->{
                Screening changes_screening = (Screening) event.getMessage().getObject();
                if(Screening_ID.getText().trim().isEmpty())
                {
                    return;
                }
                if(Integer.parseInt(Screening_ID.getText().trim()) ==  changes_screening.getAuto_number_screening())
                {
                    theater_map.setText(changes_screening.getTheater_map());
                }
            });
        }
        else if (event.getId()==BaseEventBox.get_event_id("DELETE_PAST_SCREENING")) {
            List<Screening> toDelete = (List<Screening>) event.getMessage().getObject();
            if(list == null)
                return;
            List<Screening>list_copy = list;

            Iterator<Screening> iterator = list_copy.iterator();
            while (iterator.hasNext()) {
                Screening s = iterator.next();
                for (Screening d : toDelete) {
                    if (s.getAuto_number_screening() == d.getAuto_number_screening()) {
                        iterator.remove();  // Safely remove the element using Iterator
                        break; // Exit the inner loop if a match is found and removal is done
                    }
                }
            }

            event.getMessage().setObject(list_copy);
            Platform.runLater(()->{
                get_data(event.getMessage());
            });
        }
    }

    private ObservableList<Screening> list;

    @Subscribe
    public void update_event(BaseEventBox event)
    {
        if(event.getId() == 11) {
            search_branch_function();
        }
    }

    @FXML
    void select_screening(MouseEvent event) {
        ErrorMessage.setVisible(false);
        int index = table_view.getSelectionModel().getSelectedIndex();
        if(index <= -1)
            return;
        SimpleDateFormat dateFormatC = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormatC = new SimpleDateFormat("HH:mm");
        Screening_ID.setText(id_column.getCellData(index).toString());
        Branch.setValue(branch_column.getCellData(index).toString());
        Date timec = screening_time_column.getCellData(index);
        screening_time.setText(timeFormatC.format(timec));
        Date datec = date_column.getCellData(index);
        date.setText(dateFormatC.format(datec));
        room_number.setText(room_column.getCellData(index).toString());

        List<String> key = Arrays.asList(branch_column.getCellData(index).toString(),room_column.getCellData(index).toString());
        List<Integer> row_col= SimpleChatClient.get_rows_and_columns(key);
        if(row_col != null) {
            rows_number.setText(row_col.get(0).toString());
            column_number.setText(row_col.get(1).toString());
        }
        else
        {
            rows_number.setText("");
            column_number.setText("");
        }
        Message message = new Message(4,"#get_screening_from_id");
        message.setObject(Integer.parseInt(id_column.getCellData(index).toString()));
        try {
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Subscribe
    public void printServerError(BaseEventBox event)
    {
        if(event.getId()==12) {
            Platform.runLater(() -> {
                ErrorMessage.setVisible(true);
                ErrorMessage.setText(event.getMessage().getData());
            });
        }
    }

    private static Screening current_screening;
    @Subscribe
    public void update_boxes(BaseEventBox event)
    {
        if(event.getId()==10) {

            Platform.runLater(() -> {
                Screening screening = (Screening) event.getMessage().getObject();
                current_screening = screening;
                Screening_ID.setText(Integer.toString(current_screening.getAuto_number_screening()));
                theater_map.setText(screening.getTheater_map());
            });
        }
    }
    @FXML
    void back_to_catalog(ActionEvent event) {
        ErrorMessage.setVisible(false);
        //EventBus.getDefault().unregister(this);
        Platform.runLater(() -> {
				SimpleChatClient.setWindowTitle("editing_details");
				try {
					SimpleChatClient.setRoot("Movie_editing_details");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});

    }

    @FXML
    void get_row_column(KeyEvent event) {
        ErrorMessage.setVisible(false);
        if (Branch.getValue() == null)
        {
            return;
        }
        List<String> key = Arrays.asList(Branch.getValue().toString(),room_number.getText().toString());
        List<Integer> row_col= SimpleChatClient.get_rows_and_columns(key);
        if(row_col != null) {
            rows_number.setText(row_col.get(0).toString());
            column_number.setText(row_col.get(1).toString());
        }
        else
        {
            rows_number.setText("");
            column_number.setText("");
        }


    }
    @FXML
    void change_branch(ActionEvent event) {
        ErrorMessage.setVisible(false);
        if (Branch.getValue() == null)
        {
            return;
        }
        if(room_number.getText() ==null||room_number.getText().isEmpty())
        {
            return;
        }
        List<String> key = Arrays.asList(Branch.getValue().toString(),room_number.getText().toString());
        List<Integer> row_col= SimpleChatClient.get_rows_and_columns(key);
        if(row_col != null) {
            rows_number.setText(row_col.get(0).toString());
            column_number.setText(row_col.get(1).toString());
        }
        else
        {
            rows_number.setText("");
            column_number.setText("");
        }

    }

    private void get_data(Message m){

        id_column.setCellValueFactory(new PropertyValueFactory<>("auto_number_screening"));
        branch_column.setCellValueFactory(new PropertyValueFactory<>("branch"));
        screening_time_column.setCellValueFactory(new PropertyValueFactory<>("date_time"));
        screening_time_column.setCellFactory(column -> new TextFieldTableCell<>(new DateStringConverter("HH:mm")));
        date_column.setCellValueFactory(new PropertyValueFactory<>("date_time"));
        date_column.setCellFactory(column -> new TextFieldTableCell<>(new DateStringConverter("dd/MM/yyyy")));
        room_column.setCellValueFactory(new PropertyValueFactory<>("room_number"));
        list = FXCollections.observableArrayList((List<Screening>)(m.getObject()));
        table_view.setItems(list);
    }

    private  Movie current_movie;
    @FXML
    public void initialize() {
        EventBus.getDefault().register(this);
        current_movie = go_to_screening_movie;
        Movie_name.setText(current_movie.getMovie_name());
        Branch.getItems().clear();
        Branch.getItems().add("Sakhnin");
        Branch.getItems().add("Haifa");
        Branch.getItems().add("Nazareth");
        Branch.getItems().add("Nhif");
        search_branch_combobox.getItems().clear();
        search_branch_combobox.getItems().add("");
        search_branch_combobox.getItems().add("Sakhnin");
        search_branch_combobox.getItems().add("Haifa");
        search_branch_combobox.getItems().add("Nazareth");
        search_branch_combobox.getItems().add("Nhif");
        get_data(Current_Message);


    }
    @Subscribe
    public void change_content1(BeginContentChangeEnent event)
    {

        System.out.println(event.getPage());
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().post(new ContentChangeEvent(event.getPage()));
    }

}
