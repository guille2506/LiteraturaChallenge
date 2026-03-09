package com.aluracursos.literalura.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "libro")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    private String temas;
    @ManyToOne(fetch = FetchType.EAGER)
    private Autor autor;
    @Enumerated(EnumType.STRING)
    private Idioma idioma;
    private Integer descargas;

    public Libro() {
    }

    public Libro(DatosLibro datosLibro) {
        this.titulo = datosLibro.titulo();
        this.temas = String.join(", ", datosLibro.temas());

        // En caso de nulls
        List<DatosAutor> autores = datosLibro.autores();
        if (autores == null || autores.isEmpty()) {
            this.autor = null;
        } else {
            this.autor = new Autor(autores.getFirst());
        }

        // En caso de nulls
        List<String> idiomas = datosLibro.idiomas();
        if (idiomas == null || idiomas.isEmpty()) {
            this.idioma = null;
        } else {
            this.idioma = Idioma.fromString(idiomas.getFirst());
        }
        this.descargas = datosLibro.descargas();
    }

    @Override
    public String toString() {
        return "Libro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", temas='" + temas + '\'' +
                ", autor=" + (autor != null ? autor.getNombreAutor() : "Sin autor") +
                ", idioma=" + idioma +
                ", descargas=" + descargas +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTemas() {
        return temas;
    }

    public void setTemas(String temas) {
        this.temas = temas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Idioma getIdioma() {
        return idioma;
    }

    public void setIdioma(Idioma idioma) {
        this.idioma = idioma;
    }

    public Integer getDescargas() {
        return descargas;
    }

    public void setDescargas(Integer descargas) {
        this.descargas = descargas;
    }
}
