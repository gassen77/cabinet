package JsfClasses.util;

import java.util.ArrayList;
import java.util.List;
import javax.faces.model.DataModel;

public abstract class PaginationHelper {

  private int pageSize;
    private int page;
    private String requetteJpql;
    private List<String> arrayNames;
    private List<Object> arrayValues;

    public PaginationHelper(int pageSize,String requette,List<String>s,List<Object>o) {
        this.pageSize = pageSize;
        this.requetteJpql=requette;
        this.arrayNames=s;
        this.arrayValues=o;
        this.page=0;
    }
    public PaginationHelper(int pageSize) {
        this.pageSize = pageSize;
        this.requetteJpql="";
        this.arrayNames=new ArrayList<String>();
        this.arrayValues=new ArrayList<Object>();
        this.page=0;
    }

    public abstract int getItemsCount();

    public abstract DataModel createPageDataModel();

    public int getPageFirstItem() {
        return page * pageSize;
    }

    public int getPageLastItem() {
        int i = getPageFirstItem() + pageSize - 1;
        int count = getItemsCount() - 1;
        if (i > count) {
            i = count;
        }
        if (i < 0) {
            i = 0;
        }
        return i;
    }

    public boolean isHasNextPage() {
        return (page + 1) * pageSize + 1 <= getItemsCount();
    }

    public void nextPage() {
        if (isHasNextPage()) {
            page++;
        }
    }

    public boolean isHasPreviousPage() {
        return page > 0;
    }

    public void previousPage() {
        if (isHasPreviousPage()) {
            page--;
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getRequetteJpql() {
        return requetteJpql;
    }

    public void setRequetteJpql(String requetteJpql) {
        this.requetteJpql = requetteJpql;
    }

    public List<String> getArrayNames() {
        return arrayNames;
    }

    public void setArrayNames(List<String> arrayNames) {
        this.arrayNames = arrayNames;
    }

    public List<Object> getArrayValues() {
        return arrayValues;
    }

    public void setArrayValues(List<Object> arrayValues) {
        this.arrayValues = arrayValues;
    }
    public int currentPage(){
        return this.page+1;
    }
    public int totalPages(){
        int count=this.getItemsCount();
        int x=count/this.pageSize;
        if(x==0){
            x++;
        }else{
            if(count%this.pageSize>0){
                x++;
            }
        }
        return x;
    }
    
    public boolean haveNext(){
        if(page<totalPages()-1){
            return true;
        }else{
            return false;
        }
    }
    public boolean havePrevious(){
        if(page>0){
            return true;
        }else{
            return false;
        }
    }
}
