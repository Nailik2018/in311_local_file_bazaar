package ch.hftm.entity;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class BlogDto {

    private String title;
    private String content;

    public BlogDto() {
    }

    public BlogDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setContent(String content){
        this.content = content;
    }
}