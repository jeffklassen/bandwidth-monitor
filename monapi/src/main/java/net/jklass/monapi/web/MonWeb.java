package net.jklass.monapi.web;

import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@WebService
@Path("/")
public interface MonWeb
{

	@GET
	@Path("getdays/{num}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDays(@PathParam("num") int num);

	@GET
	@Path("getdetails/{startDateTime}/{endDateTime}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response getDetails(
			@PathParam("startDateTime") String startDateTime,
			@PathParam("endDateTime") String endDateTime);

	@GET
	@Path("gethours/{num}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHours(@PathParam("num") int num);

	@GET
	@Path("getminutes/{num}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMinutes(@PathParam("num") int num);

	/**
	 * Endpoint definition for deleting a comment
	 * 
	 * @param id
	 * @return
	 */
	@GET
	@Path("getseconds/{num}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSeconds(@PathParam("num") int num);

	@GET
	@Path("gettotal/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTotal();

}
