package co.songliao.guitvi;

/**
 * Created by Song on 1/5/15.
 */
public class Song {

    private String title;
    private String singer;
    private String album;
    private String lyrics;

    public Song (String title, String singer, String album, String lyrics){
        super();
        this.title = title;
        this.singer = singer;
        this.album = album;
        this.lyrics = lyrics;

    }

    public String getTitle(){
        return title;
    }
    public String getSinger(){
        return singer;
    }
    public String getAlbum(){
        return album;
    }
    public String getLyrics(){
        return lyrics;
    }

}
