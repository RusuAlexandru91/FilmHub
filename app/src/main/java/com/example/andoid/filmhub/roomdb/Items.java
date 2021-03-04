package com.example.andoid.filmhub.roomdb;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "filmhub_db_table")
public class Items {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "item_id")
    private final String itemid;

    @ColumnInfo(name = "title")
    private final String itemTitle;

    @ColumnInfo(name = "backdrop")
    private final String backdrop;

    @ColumnInfo(name = "overview")
    private final String overview;

    @ColumnInfo(name = "is_movie")
    private final boolean isMovie;

    public Items(String itemid, String itemTitle, String backdrop, String overview, boolean isMovie) {
        this.itemid = itemid;
        this.itemTitle = itemTitle;
        this.backdrop = backdrop;
        this.overview = overview;
        this.isMovie = isMovie;
    }


    public String getItemTitle() {
        return itemTitle;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public String getOverview() {
        return overview;
    }

    public boolean isMovie() {
        return isMovie;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUid() {
        return uid;
    }

    public String getItemid() {
        return itemid;
    }

    @NotNull
    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", itemTitle='" + itemTitle + '\'' +
                ", backdrop='" + backdrop + '\'' +
                ", overview='" + overview + '\'' +
                ", saved=" + isMovie +
                '}';
    }
}
