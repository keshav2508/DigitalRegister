package ds.docusheet.table.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "saveddata")
public class SavedDataEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String OfflineDataId;
    private String column1;
    private String column2;
    private String column3;
    private String column4;
    private String column5;
    private String column6;
    private String column7;
    private String column8;
    private String column9;
    private String column10;
    private String column11;
    private String column12;
    private String column13;
    private String column14;
    private String column15;
    private String column16;
    private String column17;
    private String column18;
    private String column19;
    private String column20;
    private String height;
    private String width;
    private String doc_id;
    private String OfflineDocId;
    private String serial_no;

    public SavedDataEntity(long id,String OfflineDataId, String column1, String column2, String column3, String column4, String column5, String column6, String column7, String column8, String column9, String column10, String column11, String column12, String column13, String column14, String column15, String column16, String column17, String column18, String column19, String column20, String height, String width, String doc_id,String OfflineDocId, String serial_no) {
        this.id = id;
        this.OfflineDataId = OfflineDataId;
        this.column1 = column1;
        this.column2 = column2;
        this.column3 = column3;
        this.column4 = column4;
        this.column5 = column5;
        this.column6 = column6;
        this.column7 = column7;
        this.column8 = column8;
        this.column9 = column9;
        this.column10 = column10;
        this.column11 = column11;
        this.column12 = column12;
        this.column13 = column13;
        this.column14 = column14;
        this.column15 = column15;
        this.column16 = column16;
        this.column17 = column17;
        this.column18 = column18;
        this.column19 = column19;
        this.column20 = column20;
        this.height = height;
        this.width = width;
        this.doc_id = doc_id;
        this.OfflineDocId = OfflineDocId;
        this.serial_no = serial_no;
    }

    public SavedDataEntity() {
    }

    public String getOfflineDataId() {
        return OfflineDataId;
    }

    public void setOfflineDataId(String offlineDataId) {
        OfflineDataId = offlineDataId;
    }

    public String getOfflineDocId() {
        return OfflineDocId;
    }

    public void setOfflineDocId(String offlineDocId) {
        OfflineDocId = offlineDocId;
    }

    public void setId(long id) {
        this.id = id;
    }
    public void setColumn(String column,int column_no){
        if(column_no==1){
            this.column1=column;
        }else if(column_no==2 ){
            this.column2=column;
        }else if(column_no== 3){
            this.column3=column;
        }else if(column_no==4 ){
            this.column4=column;
        }else if(column_no==5 ){
            this.column5=column;
        }else if(column_no==6 ){
            this.column6=column;
        }else if(column_no== 7){
            this.column7=column;
        }else if(column_no== 8){
            this.column8=column;
        }else if(column_no== 9){
            this.column9=column;
        }else if(column_no==10 ){
            this.column10=column;

        }else if(column_no==11 ){
            this.column11=column;

        }else if(column_no==12 ){
            this.column12=column;

        }else if(column_no==13 ){
            this.column13=column;

        }else if(column_no==14 ){
            this.column14=column;

        }else if(column_no== 15){
            this.column15=column;

        }else if(column_no== 16){
            this.column16=column;

        }else if(column_no== 17){
            this.column17=column;

        }else if(column_no==18 ){
            this.column18=column;

        }else if(column_no== 19){
            this.column19=column;

        }else if(column_no==20 ){
            this.column20=column;

        }
    }
    public String getColumn(int column_no){
        if(column_no==1){
            return column1;
        }else if(column_no==2 ){
            return column2;
        }else if(column_no== 3){
            return column3;
        }else if(column_no==4 ){
            return column4;
        }else if(column_no==5 ){
            return column5;
        }else if(column_no==6 ){
            return column6;

        }else if(column_no== 7){
            return column7;

        }else if(column_no== 8){
            return column8;

        }else if(column_no== 9){
            return column9;

        }else if(column_no==10 ){
            return column10;

        }else if(column_no==11 ){
            return column11;

        }else if(column_no==12 ){
            return column12;

        }else if(column_no==13 ){
            return column13;

        }else if(column_no==14 ){
            return column14;

        }else if(column_no== 15){
            return column15;

        }else if(column_no== 16){
            return column16;

        }else if(column_no== 17){
            return column17;

        }else if(column_no==18 ){
            return column18;

        }else if(column_no== 19){
            return column19;

        }else if(column_no==20 ){
            return column20;
        }
        return null;
    }

    public void setColumn1(String column1) {
        this.column1 = column1;
    }

    public void setColumn2(String column2) {
        this.column2 = column2;
    }

    public void setColumn3(String column3) {
        this.column3 = column3;
    }

    public void setColumn4(String column4) {
        this.column4 = column4;
    }

    public void setColumn5(String column5) {
        this.column5 = column5;
    }

    public void setColumn6(String column6) {
        this.column6 = column6;
    }

    public void setColumn7(String column7) {
        this.column7 = column7;
    }

    public void setColumn8(String column8) {
        this.column8 = column8;
    }

    public void setColumn9(String column9) {
        this.column9 = column9;
    }

    public void setColumn10(String column10) {
        this.column10 = column10;
    }

    public void setColumn11(String column11) {
        this.column11 = column11;
    }

    public void setColumn12(String column12) {
        this.column12 = column12;
    }

    public void setColumn13(String column13) {
        this.column13 = column13;
    }

    public void setColumn14(String column14) {
        this.column14 = column14;
    }

    public void setColumn15(String column15) {
        this.column15 = column15;
    }

    public void setColumn16(String column16) {
        this.column16 = column16;
    }

    public void setColumn17(String column17) {
        this.column17 = column17;
    }

    public void setColumn18(String column18) {
        this.column18 = column18;
    }

    public void setColumn19(String column19) {
        this.column19 = column19;
    }

    public void setColumn20(String column20) {
        this.column20 = column20;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

    public long getId() {
        return id;
    }

    public String getColumn1() {
        return column1;
    }

    public String getColumn2() {
        return column2;
    }

    public String getColumn3() {
        return column3;
    }

    public String getColumn4() {
        return column4;
    }

    public String getColumn5() {
        return column5;
    }

    public String getColumn6() {
        return column6;
    }

    public String getColumn7() {
        return column7;
    }

    public String getColumn8() {
        return column8;
    }

    public String getColumn9() {
        return column9;
    }

    public String getColumn10() {
        return column10;
    }

    public String getColumn11() {
        return column11;
    }

    public String getColumn12() {
        return column12;
    }

    public String getColumn13() {
        return column13;
    }

    public String getColumn14() {
        return column14;
    }

    public String getColumn15() {
        return column15;
    }

    public String getColumn16() {
        return column16;
    }

    public String getColumn17() {
        return column17;
    }

    public String getColumn18() {
        return column18;
    }

    public String getColumn19() {
        return column19;
    }

    public String getColumn20() {
        return column20;
    }

    public String getHeight() {
        return height;
    }

    public String getWidth() {
        return width;
    }

    public String getDoc_id() {
        return doc_id;
    }

    public String getSerial_no() {
        return serial_no;
    }
}
