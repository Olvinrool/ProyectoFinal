package hn.uth.myapplication.planilla;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "planilla_table")
public class Planilla {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private Integer idPlanilla;
    @NonNull
    @ColumnInfo(name = "concepto")
    private String concepto;
    @NonNull
    @ColumnInfo(name = "fecha")
    private String fecha;
    @ColumnInfo(name = "totals")
    private int totals;

    public Planilla(@NonNull String concepto, @NonNull String fecha, int totals) {
        this.concepto = concepto;
        this.fecha = fecha;
        this.totals = totals;
    }

    @NonNull
    public Integer getIdPlanilla() {
        return idPlanilla;
    }

    public void setIdPlanilla(@NonNull Integer idPlanilla) {
        this.idPlanilla = idPlanilla;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getTotals() {
        return totals;
    }

    public void setTotals(int totals) {
        this.totals = totals;
    }
}
