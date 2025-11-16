package org.fase2.dwf2.controller;

import jakarta.ws.rs.*;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.fase2.dwf2.dto.Branch.BranchResponseDto;
import org.fase2.dwf2.entities.Branch;
import org.fase2.dwf2.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Path("/api/branches")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BranchController {

    private final BranchService branchService;

    @Autowired
    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @POST
    @Path("/new-branch")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBranch(BranchResponseDto branchResponseDto) {
        try {
            BranchResponseDto createdBranch = branchService.createBranch(branchResponseDto);
            return Response.status(Response.Status.CREATED).entity(createdBranch).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to create branch.").build();
        }
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBranches() {
        List<BranchResponseDto> branches = branchService.getAllBranches();
        return Response.ok(branches).build();
    }


    @GET
    @Path("/unassigned")
    public Response getUnassignedBranches() {
        System.out.println("Getting unassigned branches");
        List<BranchResponseDto> unassignedBranches = branchService.getUnassignedBranches();
        return Response.ok(unassignedBranches).build();
    }

    @PUT
    @Path("/{branchId}/assign/{managerId}")
    public Response assignManagerToBranch(@PathParam("branchId") Long branchId, @PathParam("managerId") Long managerId) {
        try {
            Branch updatedBranch = branchService.assignManagerToBranch(branchId, managerId);
            return Response.ok(updatedBranch).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }


}
