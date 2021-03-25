package ds.docusheet.table.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DaoDocument {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(DocumentEntity document);

    @Query("select * from document where details_user_id = :user_id AND OfflineDocId = null")
    List<DocumentEntity> getAllDocuments(String user_id);

    @Query("select * from document where details_user_id = :user_id AND NOT OfflineDocId = null")
    List<DocumentEntity> getAllDocumentsNotOnline(String user_id);

    @Query("delete from document where doc_id= :id")
    void delete(long id);

    @Query("update document set doc_name = :name where doc_id = :id")
    void updateName(String name,long id);

}
