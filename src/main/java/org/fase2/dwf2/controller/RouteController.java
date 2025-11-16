package org.fase2.dwf2.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.net.URI;

@Path("/")
public class RouteController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @GET
    @Path("/")
    public Response getRoot() {
        SecurityContextHolder.clearContext();
        return Response.ok(new File("src/main/resources/static/login.html")).build();
    }

    @GET
    @Path("/login")
    public Response getLogin() {
        // Clear any previous authentication to ensure a fresh login session
        SecurityContextHolder.clearContext();
        return Response.ok(new File("src/main/resources/static/login.html")).build();
    }

    @GET
    @Path("/logout")
    public Response logout() {
        // Obtener el objeto de autenticación actual del SecurityContext
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            // Logout y limpiar la sesión actual
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        // Redireccionar al login
        return Response.seeOther(URI.create("/login")).build();
    }

    @GET
    @Path("/register")
    public Response getRegister() {
        return Response.ok(new File("src/main/resources/static/reg-new-client.html")).build();
    }

    @GET
    @Path("/unauthorized")
    public Response getUnauthorized() {
        return Response.ok(new File("src/main/resources/static/unauthorized.html")).build();
    }

    @GET
    @Path("/access-denied")
    public Response getAccessDenied() {
        return Response.ok(new File("src/main/resources/static/access-denied.html")).build();
    }

    // EMPIEZAN CLIENTES

    @GET
    @Path("/client/dashboard")
    @RolesAllowed("CLIENT")
    public Response getClientDashboard() {
        System.out.println("Client dashboard");
        return Response.ok(new File("src/main/resources/static/cliente/dashboard.html")).build();
    }

    @GET
    @Path("/client/deposit-funds")
    @RolesAllowed("CLIENT")
    public Response getClientDepositFunds() {
        return Response.ok(new File("src/main/resources/static/cliente/deposit-funds.html")).build();
    }

    @GET
    @Path("/client/withdraw-funds")
    @RolesAllowed("CLIENT")
    public Response getClientWithdrawFunds() {
        return Response.ok(new File("src/main/resources/static/cliente/withdraw-funds.html")).build();
    }

    @GET
    @Path("/client/make-transaction")
    @RolesAllowed("CLIENT")
    public Response getClientMakeTransaction() {
        return Response.ok(new File("src/main/resources/static/cliente/make-transaction.html")).build();
    }

    @GET
    @Path("/client/transaction-history")
    @RolesAllowed("CLIENT")
    public Response getClientTransactionHistory() {
        return Response.ok(new File("src/main/resources/static/cliente/transaction-history.html")).build();
    }

    // TERMINA CLIENTES

//    EMPIEZA DEPENDIENTES
    @GET
    @Path("/dependiente/dashboard")
    @RolesAllowed("DEPENDIENTE")
    public Response getDependienteDashboard() {
        return Response.ok(new File("src/main/resources/static/dependiente/dashboard.html")).build();
    }

    @GET
    @Path("/dependiente/deposit-funds")
    @RolesAllowed("DEPENDIENTE")
    public Response getDependienteDepositFunds() {
        return Response.ok(new File("src/main/resources/static/dependiente/deposit-funds.html")).build();
    }

    @GET
    @Path("/dependiente/withdraw-funds")
    @RolesAllowed("DEPENDIENTE")
    public Response getDependienteWithdrawFunds() {
        return Response.ok(new File("src/main/resources/static/dependiente/withdraw-funds.html")).build();
    }

    @GET
    @Path("/dependiente/make-transaction")
    @RolesAllowed("DEPENDIENTE")
    public Response getDependienteMakeTransaction() {
        return Response.ok(new File("src/main/resources/static/dependiente/make-transaction.html")).build();
    }

    @GET
    @Path("/dependiente/transaction-history")
    @RolesAllowed("DEPENDIENTE")
    public Response getDependienteTransactionHistory() {
        return Response.ok(new File("src/main/resources/static/dependiente/transaction-history.html")).build();
    }

    @GET
    @Path("/dependiente/special-services")
    @RolesAllowed("DEPENDIENTE")
    public Response getDependienteSpecialServices() {
        return Response.ok(new File("src/main/resources/static/dependiente/special-services.html")).build();
    }

    // TERMINA DEPENDIENTES

    // EMPIEZAN CAJEROS

    @GET
    @Path("/cajero/dashboard")
    @RolesAllowed("CAJERO")
    public Response getCajeroDashboard() {
        return Response.ok(new File("src/main/resources/static/cajero/dashboard.html")).build();
    }

    @GET
    @Path("/cajero/manage-loans")
    @RolesAllowed("CAJERO")
    public Response getCajeroManagedLoans() {
        return Response.ok(new File("src/main/resources/static/cajero/manage-loaners-funds.html")).build();
    }

    @GET
    @Path("/cajero/open-loan")
    @RolesAllowed("CAJERO")
    public Response getCajeroOpenLoans() {
        return Response.ok(new File("src/main/resources/static/cajero/open-loan.html")).build();
    }

    @GET
    @Path("/cajero/register-client")
    @RolesAllowed("CAJERO")
    public Response getCajeroRegisterClient() {
        return Response.ok(new File("src/main/resources/static/cajero/register-client.html")).build();
    }

    // TERMINA CAJEROS

    // EMPIEZA GERENTE SUCURSAL

    @GET
    @Path("/gerente_sucursal/dashboard")
    @RolesAllowed("GERENTE_SUCURSAL")
    public Response getGerenteSucursalDashboard() {
        return Response.ok(new File("src/main/resources/static/gerente-sucursal/dashboard.html")).build();
    }

    @GET
    @Path("/gerente_sucursal/employees")
    @RolesAllowed("GERENTE_SUCURSAL")
    public Response getGerenteSucursalManageEmployees() {
        return Response.ok(new File("src/main/resources/static/gerente-sucursal/employees.html")).build();
    }

    @GET
    @Path("/gerente_sucursal/loans")
    @RolesAllowed("GERENTE_SUCURSAL")
    public Response getGerenteSucursalOngoingLoans() {
        return Response.ok(new File("src/main/resources/static/gerente-sucursal/loans.html")).build();
    }

    // TERMINA GERENTE SUCURSAL

    // EMPIEZA GERENTE GENERAL

    @GET
    @Path("/gerente_general/dashboard")
    @RolesAllowed("GERENTE_GENERAL")
    public Response getGerenteGeneralDashboard() {
        return Response.ok(new File("src/main/resources/static/gerente-general/dashboard.html")).build();
    }

    @GET
    @Path("/gerente_general/employee_actions")
    @RolesAllowed("GERENTE_GENERAL")
    public Response getGerenteGeneralEmployeeActions() {
        return Response.ok(new File("src/main/resources/static/gerente-general/employee-actions.html")).build();
    }

    @GET
    @Path("/gerente_general/transactions-overview")
    @RolesAllowed("GERENTE_GENERAL")
    public Response getGerenteGeneralTransactionsOverview() {
        return Response.ok(new File("src/main/resources/static/gerente-general/transactions-overview.html")).build();
    }

    @GET
    @Path("/gerente_general/franchise-management")
    @RolesAllowed("GERENTE_GENERAL")
    public Response getGerenteGeneralFranchiseManagement() {
        return Response.ok(new File("src/main/resources/static/gerente-general/franchise-management.html")).build();
    }

}
