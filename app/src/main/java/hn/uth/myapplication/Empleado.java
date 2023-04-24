package hn.uth.myapplication;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Empleados_Table")
public class Empleado {


    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private Integer idEmpleado;
    @NonNull
    @ColumnInfo(name = "nombre")
    private String nombre;
    @NonNull
    @ColumnInfo(name = "apellido")
    private String apellido;
    @ColumnInfo(name = "salario")
    private int salario;


    public Empleado(@NonNull String nombre, @NonNull String apellido, int salario) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.salario = salario;
    }

    @NonNull
    public Integer getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(@NonNull Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    @NonNull
    public String getNombre() {
        return nombre;
    }

    public void setNombre(@NonNull String nombre) {
        this.nombre = nombre;
    }

    @NonNull
    public String getApellido() {
        return apellido;
    }

    public void setApellido(@NonNull String apellido) {
        this.apellido = apellido;
    }

    public int getSalario() {
        return salario;
    }

    public void setSalario(int salario) {
        this.salario = salario;
    }






}


