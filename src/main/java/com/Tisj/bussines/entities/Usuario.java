package com.Tisj.bussines.entities;

import com.Tisj.bussines.entities.DT.DTUsuario;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    private String email;
    private String password;
    private String nombre;
    private String apellido;
    private char genero;
    private LocalDate nacimiento;
    private String emailRecuperacion;
    private Boolean activo;

    @ManyToMany (fetch = FetchType.EAGER)
    @JoinTable(name = "USUARIOS_ROLES",
            joinColumns = @JoinColumn(name = "USUARIO"),
            inverseJoinColumns = @JoinColumn(name = "ID_ROL"))
    @ToString.Exclude
    private List<RolUsuario> roles;

    @OneToMany
    @JoinColumn(name = "articulo_cliente_id")
    private List<ArticuloCliente> articulos;

    @OneToMany (mappedBy = "usuario")
    private List<Pago> pagos;

    public Void modificarContrasena(){
        return null;
    }
    public Void recuperarCuenta(String email){
        return null;
    }

    public DTUsuario crearDT(){
        List<String> rolesStr = this.roles.stream()
                .map(RolUsuario::getNombre)
                .toList();
        return new DTUsuario(email, nombre, apellido, genero, nacimiento, activo, rolesStr);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Usuario usuario = (Usuario) o;
        return getEmail() != null && Objects.equals(getEmail(), usuario.getEmail());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
