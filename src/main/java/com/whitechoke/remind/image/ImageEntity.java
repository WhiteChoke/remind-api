package com.whitechoke.remind.image;

import com.whitechoke.remind.storage.Storable;
import com.whitechoke.remind.word.userWord.UserWordEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "images")
public class ImageEntity implements Storable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "image_path", nullable = false)
    private String imagePath;

    @OneToOne(mappedBy = "image")
    private UserWordEntity userWord;

    @Override
    public void setPath(String path) {
        this.imagePath = path;
    }
}
