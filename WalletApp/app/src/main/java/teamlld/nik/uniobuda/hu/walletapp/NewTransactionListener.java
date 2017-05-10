package teamlld.nik.uniobuda.hu.walletapp;

import teamlld.nik.uniobuda.hu.walletapp.models.Transaction;

/**
 * Created by GERGO on 2017.04.01..
 */

public interface NewTransactionListener {
    void NewTransactionAdded(Transaction transaction);
}
