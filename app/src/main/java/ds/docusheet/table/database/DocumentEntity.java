package ds.docusheet.table.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "document")
public class DocumentEntity {

    @PrimaryKey(autoGenerate = true)
    long doc_id;
    String OfflineDocId;
    String doc_name;
    String details_user_id;
    String update;

    public DocumentEntity() {

    }
    public DocumentEntity(long doc_id,String OfflineDocId, String doc_name, String details_user_id, String update) {
        this.doc_id = doc_id;
        this.OfflineDocId = OfflineDocId;
        this.doc_name = doc_name;
        this.details_user_id = details_user_id;
        this.update = update;
    }

    public String getOfflineDocId() {
        return OfflineDocId;
    }

    public void setOfflineDocId(String offlineDocId) {
        OfflineDocId = offlineDocId;
    }

    public long getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(long doc_id) {
        this.doc_id = doc_id;
    }

    public String getDoc_name() {
        return doc_name;
    }

    public void setDoc_name(String doc_name) {
        this.doc_name = doc_name;
    }

    public String getDetails_user_id() {
        return details_user_id;
    }

    public void setDetails_user_id(String details_user_id) {
        this.details_user_id = details_user_id;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

}
