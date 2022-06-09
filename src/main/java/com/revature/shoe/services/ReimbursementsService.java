package com.revature.shoe.services;

import com.revature.shoe.daos.ReimbursementsDAO;
import com.revature.shoe.util.annotations.Inject;

public class ReimbursementsService {
    @Inject
    private final ReimbursementsDAO reimbursementsDAO;

    public ReimbursementsService(ReimbursementsDAO reimbursementsDAO) {
        this.reimbursementsDAO = reimbursementsDAO;
    }
}
