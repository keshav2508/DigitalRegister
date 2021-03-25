package ds.docusheet.table.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DaoColumnDetails {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ColumnDetailsEntity data);

    @Query("select * from column_details where doc_id= :doc_id")
    List<ColumnDetailsEntity> getAllDetails(long doc_id);

    @Query("select * from column_details where doc_id= :doc_id")
    List<ColumnDetailsEntity> getAllOfflineDetails(long doc_id);

    @Query("update column_details set column_type= :col_type  where col_id= :col_id")
    void updateColumnType(long col_id, String col_type);

    @Query("update column_details set column_names= :col_name  where col_id= :col_id")
    void updateColumnName(long col_id, String col_name);

    @Query("delete from column_details where col_id= :col_id")
    void delete(long col_id);

    @Query("update column_details set formula= :formula  where col_id= :col_id")
    void updateColumnFormula(long col_id, String formula);

    @Query("update column_details set total= :total  where col_id= :col_id")
    void updateColumnTotal(long col_id, String total);
}
