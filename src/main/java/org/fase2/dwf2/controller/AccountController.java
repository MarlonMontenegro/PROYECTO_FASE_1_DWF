package org.fase2.dwf2.controller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.fase2.dwf2.dto.Account.AccountRequestDto;
import org.fase2.dwf2.service.AccountService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Path("/api/account")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GET
    @Path("/{email}")
    public Response getAccount(@PathParam("email") String email) {
        List<AccountRequestDto> accounts = accountService.findByUserEmail(email);
        return Response.ok(accounts).build();
    }

    @POST
    public Response createAccount(AccountRequestDto accountRequestDto) {
        AccountRequestDto account = accountService.createAccount(accountRequestDto);
        return Response.ok(account).build();
    }

}
