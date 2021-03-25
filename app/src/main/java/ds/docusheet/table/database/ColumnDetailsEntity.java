package ds.docusheet.table.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "column_details")
public class ColumnDetailsEntity {

    @PrimaryKey(autoGenerate = true)
    long col_id;
    String OfflineColId;
    String column_names;
    String column_nums;
    String column_type;
    String doc_id;
    String OfflineDocId;
    String total;
    String formula;

    public long getCol_id() {
        return col_id;
    }

    public void setCol_id(long col_id) {
        this.col_id = col_id;
    }

    public String getColumn_names() {
        return column_names;
    }

    public void setColumn_names(String column_names) {
        this.column_names = column_names;
    }

    public String getColumn_nums() {
        return column_nums;
    }

    public void setColumn_nums(String column_nums) {
        this.column_nums = column_nums;
    }

    public String getColumn_type() {
        return column_type;
    }

    public void setColumn_type(String column_type) {
        this.column_type = column_type;
    }

    public String getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getOfflineColId() {
        return OfflineColId;
    }

    public void setOfflineColId(String offlineColId) {
        OfflineColId = offlineColId;
    }

    public ColumnDetailsEntity(long col_id, String OfflineColId, String column_names, String column_nums, String column_type,
                               String doc_id,String OfflineDocId, String total, String formula) {
        this.col_id = col_id;
        this.OfflineColId = OfflineColId;
        this.column_names = column_names;
        this.column_nums = column_nums;
        this.column_type = column_type;
        this.doc_id = doc_id;
        this.OfflineDocId= OfflineDocId;
        this.total = total;
        this.formula = formula;
    }

    public ColumnDetailsEntity() {
    }
}
