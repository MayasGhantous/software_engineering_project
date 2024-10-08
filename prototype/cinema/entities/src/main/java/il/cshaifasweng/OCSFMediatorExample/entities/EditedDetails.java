package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.DataInput;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.File;

@Entity
@Table(name = "EditedDetails")
public class EditedDetails implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int auto_number_edited_details;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "movie_id", referencedColumnName = "auto_number_movie")
    private Movie movie;

    private double changed_price;

    // Constructors
    public EditedDetails() {}

    public EditedDetails(int auto_number_edited_details,Movie movie, double changed_price) {
        this.auto_number_edited_details = auto_number_edited_details;
        this.movie = movie;
        this.changed_price = changed_price;
    }

    public int getAuto_number_edited_details() {
        return auto_number_edited_details;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public double getChanged_price() {
        return changed_price;
    }

    public void setChanged_price(double changed_price) {
        this.changed_price = changed_price;
    }
}