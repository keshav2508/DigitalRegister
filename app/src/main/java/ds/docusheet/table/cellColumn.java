package ds.docusheet.table;

public class cellColumn {
    int col_id;
    String column_name;
    String column_type;
    int colIndex;

    public cellColumn(int col_id, String column_name, String column_type,int colIndex) {
        this.col_id = col_id;
        this.column_name = column_name;
        this.column_type = column_type;
        this.colIndex = colIndex;
    }
    public int getColIndex(){
        return colIndex;
    }

    public void setColIndex(int colIndex){
        this.colIndex = colIndex;
    }

    public int getCol_id() {
        return col_id;
    }

    public void setCol_id(int col_id) {
        this.col_id = col_id;
    }

    public String getColumn_name() {
        return column_name;
    }

    public void setColumn_name(String column_name) {
        this.column_name = column_name;
    }

    public String getColumn_type() {
        return column_type;
    }

    public void setColumn_type(String column_type) {
        this.column_type = column_type;
    }
}
