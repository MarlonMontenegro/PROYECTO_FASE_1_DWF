package org.fase2.dwf2.controller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.fase2.dwf2.dto.Account.AccountRequestDto;
import org.fase2.dwf2.dto.ManagedAccount.ManagedAccountDto;
import org.fase2.dwf2.dto.Transaction.TransactionRequestDto;
import org.fase2.dwf2.service.AccountService;
import org.fase2.dwf2.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Path("/api/cuentas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ManagedAccounts {

    private final AccountService accountService;
    private final TransactionService transactionService;

    public ManagedAccounts(AccountService accountService, TransactionService transactionService, PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @POST
    @Path("/nueva")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createManagedAccount(AccountRequestDto accountRequestDto) {
        try {
            ManagedAccountDto newAccount = accountService.createManagedAccount(accountRequestDto);
            return Response.ok(newAccount).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating account. Please try again later.")
                    .build();
        }
    }


    @GET
    @Path("/{dui}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getManagedAccounts(@PathParam("dui") String dependienteDui) {
        try {
            List<ManagedAccountDto> accounts = accountService.getManagedAccounts(dependienteDui);
            return Response.ok(accounts).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving managed accounts. Please try again later.")
                    .build();
        }
    }

    @POST
    @Path("/abonarefectivo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response depositToManagedAccount(TransactionRequestDto transactionRequestDto) {
        try {
            TransactionRequestDto transaction = transactionService.depositToManagedAccount(transactionRequestDto);
            return Response.ok(transaction).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error processing deposit. Please try again later.")
                    .build();
        }
    }

    @POST
    @Path("/retirarefectivo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response withdrawFromManagedAccount(TransactionRequestDto transactionRequestDto) {
        try {
            TransactionRequestDto transaction = transactionService.withdrawFromManagedAccount(transactionRequestDto);
            return Response.ok(transaction).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error processing withdrawal. Please try again later.")
                    .build();
        }
    }


}
