package booking;

import java.time.*;

import org.h2.tools.*;
import org.springframework.context.annotation.*;

import com.google.gson.*;

import booking.data.*;
import booking.service.*;
import io.muserver.*;

// Please note - there is no security in place.
// This is just a basic example how to implament CRUD
// with JPA and Spring support 
public class BookingServer {
	public static void main(String[] args) {
		// just for testing - start H2 Web Console
		try {
			Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
			System.out.println("H2 Console started at http://localhost:8082");
		} catch (Exception e) {
			System.err.println("Failed to start H2 console: " + e.getMessage());
		}

		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
		BookingService service = ctx.getBean(BookingService.class);
		Gson gson = new GsonBuilder()
				.registerTypeAdapter(LocalDate.class,
						(JsonDeserializer<LocalDate>) (json, type, context) -> LocalDate.parse(json.getAsString()))
				.registerTypeAdapter(LocalTime.class,
						(JsonDeserializer<LocalTime>) (json, type, context) -> LocalTime.parse(json.getAsString()))
				.setPrettyPrinting().create();
		MuServer server = MuServerBuilder.httpServer()
				.addHandler(Method.DELETE, "/bookings/{id}", (request, response, pathParams) -> {
					try {
						response.contentType("application/json");

						Long id = Long.parseLong(pathParams.get("id"));
						service.deleteBooking(id);
						response.status(204); // No Content
					} catch (NumberFormatException e) {
						response.status(400);
						response.write("Cannot delete Booking - Invalid ID");
					} catch (Exception e) {
						response.status(500);
						response.write("Error when deleting booking: " + e.getMessage());
					}
				}).addHandler(Method.PUT, "/bookings", (request, response, pathParams) -> {
					Booking booking = gson.fromJson(request.readBodyAsString(), Booking.class);
					if (booking == null || !booking.isValid()) {
						response.status(400);
						response.write("Booking is not valid");
						return;
					}
					// booking must have and existing ID in order to be modified
					booking = service.modifyBooking(booking);
					if (booking == null) {
						response.status(400);
						response.write("Cant find free table for updated booking.");
						return;
					}
					response.status(201);
					// send back modified booking
					response.write(gson.toJson(booking));
				}).addHandler(Method.POST, "/bookings", (request, response, pathParams) -> {
					Booking booking = gson.fromJson(request.readBodyAsString(), Booking.class);
					if (booking == null || !booking.isValid()) {
						response.status(400);
						response.write("Booking is not valid");
						return;
					}
					booking = service.addBooking(booking);
					if (booking == null) {
						response.status(400);
						response.write("Cant find free table.");
						return;
					}
					response.status(201);
					// send it back with the ID
					response.write(gson.toJson(booking));
				}).addHandler(Method.GET, "/bookings", (request, response, pathParams) -> {
					String date = request.query().get("date");
					if (date == null) {
						response.status(400);
						response.write("Please specify date.");
						return;
					}
					LocalDate localDate = LocalDate.parse(date);
					response.contentType("application/json");
					response.write(gson.toJson(service.getBookingsForDate(localDate)));
				}).withHttpPort(8080).start();

		System.out.println("Server is running:" + server.uri());
	}
}