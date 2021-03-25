package ds.docusheet.table;

import java.util.ArrayList;
import java.util.List;

class HomeFragmentDocumentChecker {
    /**
     * check = -1 list is first time loading
     * check = 1 user added or deleted one document
     * check = 0 user opened already created document
     **/
    public static int check;
    public static List<Document> documentList;
    private static HomeFragmentDocumentChecker checker;

    private HomeFragmentDocumentChecker(){
        check = -1;
        documentList = new ArrayList<>();
    }
    public static HomeFragmentDocumentChecker getInstance(){
        if (checker == null){
            checker = new HomeFragmentDocumentChecker();
        }
        return checker;
    }

    public static int getCheck() {
        return check;
    }

    public static void setCheck(int check) {
        HomeFragmentDocumentChecker.check = check;
    }

    public static List<Document> getDocumentList() {
        return documentList;
    }

    public static void setDocumentList(List<Document> documentList) {
        HomeFragmentDocumentChecker.documentList.clear();
        HomeFragmentDocumentChecker.documentList = new ArrayList<>(documentList);
    }
}
