package de.bxservice.bxpos.logic.model.idempiere;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.Serializable;

import de.bxservice.bxpos.logic.daomanager.PosTableManagement;
import de.bxservice.bxpos.logic.tasks.UpdateTableStatusTask;

/**
 * Created by Diego Ruiz on 9/11/15.
 */
public class Table implements Serializable {

    //Table status
    public static final String FREE_STATUS     = "FREE";
    public static final String BUSY_STATUS     = "BUSY";
    private static final String RESERVED_STATUS = "RESERVED";

    public static final String BAY_Table_ID = "BAY_Table_ID";

    private PosTableManagement tableManager;
    private TableGroup belongingGroup;
    private long tableID;
    private String tableName;
    private String value;
    private String status;

    public long getTableID() {
        return tableID;
    }

    public void setTableID(long tableID) {
        this.tableID = tableID;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status.equals(FREE_STATUS) ||
                status.equals(BUSY_STATUS) ||
                status.equals(RESERVED_STATUS))
            this.status = status;
    }

    public TableGroup getBelongingGroup() {
        return belongingGroup;
    }

    public void setBelongingGroup(TableGroup belongingGroup) {
        this.belongingGroup = belongingGroup;
    }

    public boolean save(Context ctx) {
        tableManager = new PosTableManagement(ctx);
        Table originalTable = tableManager.get(tableID);
        if (originalTable == null)
            return createTable();
        else {
            status = originalTable.getStatus();
            return updateTable();
        }
    }

    private boolean createTable(){
        return tableManager.create(this);
    }

    public boolean occupyTable(Context ctx) {
        status = BUSY_STATUS;
        updateServerTableStatus(ctx); //Send the new status to iDempiere
        return updateTable(ctx);
    }

    public boolean freeTable(Context ctx) {
        tableManager = new PosTableManagement(ctx);

        //Check if there are no more orders in the table -> if there are, don't free the table
        if (tableManager.isTableFree(this)) {
            status = FREE_STATUS;
            updateServerTableStatus(ctx); //Send the new status to iDempiere
            return updateTable(ctx);
        }

        return false;
    }

    public boolean reserveTable(Context ctx) {
        status = RESERVED_STATUS;
        return updateTable(ctx);
    }

    public boolean updateTable(Context ctx) {
        tableManager = new PosTableManagement(ctx);
        return updateTable();
    }

    private boolean updateTable() {
        return tableManager.update(this);
    }

    /**
     * Updates the status in iDempiere if there's internet connection
     * @param ctx
     * @return
     */
    private boolean updateServerTableStatus(Context ctx) {
        if(ctx == null)
            return false;

        //Check if network connection is available
        ConnectivityManager connMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            UpdateTableStatusTask updateTask = new UpdateTableStatusTask();
            updateTask.execute(this);
        }

        return true;

    }
}
