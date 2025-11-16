package org.fase2.dwf2.controller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.fase2.dwf2.dto.Transaction.TransactionRequestDto;
import org.fase2.dwf2.service.TransactionService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Path("/api/transaction")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTransactions() {
        List<TransactionRequestDto> transactions = transactionService.getAllTransactions();
        return Response.ok(transactions).build();
    }

    @GET
    @Path("/account/{accountNumber}")
    public Response getTransactionsByAccount(@PathParam("accountNumber") String accountNumber) {
        try {
            return Response.ok(transactionService.getTransactions(accountNumber)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while processing the transaction.")
                    .build();
        }
    }

    @POST
    @Path("/deposit")
    public Response deposit(TransactionRequestDto transactionRequestDto) {
        try {
            TransactionRequestDto transaction = transactionService.deposit(transactionRequestDto);
            return Response.ok(transaction).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while processing the transaction.")
                    .build();
        }
    }

    @POST
    @Path("/withdraw")
    public Response withdraw(TransactionRequestDto transactionRequestDto) {
        try {
            TransactionRequestDto transaction = transactionService.withdraw(transactionRequestDto);
            return Response.ok(transaction).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while processing the transaction.")
                    .build();
        }
    }

    @POST
    @Path("/transfer")
    public Response transfer(TransactionRequestDto transactionRequestDto) {
        try {
            TransactionRequestDto transaction = transactionService.transfer(transactionRequestDto);
            return Response.ok(transaction).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while processing the transaction.")
                    .build();
        }
    }

}
