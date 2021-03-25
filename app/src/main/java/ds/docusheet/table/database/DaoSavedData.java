package ds.docusheet.table.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DaoSavedData {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(SavedDataEntity data);

    @Query("select * from saveddata where doc_id= :doc_id order by serial_no+0 asc")
    List<SavedDataEntity> getDocumentSerial(long doc_id);

    @Query("update saveddata set column1= :column1,column2= :column2,column3= :column3,column4= :column4,column5= :column5," +
            "column6= :column6,column7= :column7,column8= :column8,column9= :column9,column10= :column10,column11= :column11," +
            "column12= :column12,column13= :column13,column14= :column14, column15= :column15,column16= :column16,column17= :column17," +
            "column18= :column18,column19= :column19,column20= :column20,height= :height,width= :width where id= :id")
    void updateSinleData(String column1,String column2,String column3,String column4,String column5,String column6,String column7,String column8,String column9,
    String column10,String column11,String column12,String column13,String column14,String column15,String column16,String column17,String column18,String column19
    ,String column20,String height,String width,long id);

}

