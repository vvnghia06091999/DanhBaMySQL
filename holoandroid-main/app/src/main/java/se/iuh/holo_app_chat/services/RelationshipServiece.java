package se.iuh.holo_app_chat.services;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import se.iuh.holo_app_chat.services.response.RelationshipRespone;

public interface RelationshipServiece {
    @GET("/relationships/{id}")
    Call<List<RelationshipRespone>> getRelationship(@Path(value = "id") String id);

    @GET("/relationships/FriendRequets/{id}")
    Call<List<RelationshipRespone>> getFriendRequets(@Path(value = "id") String id);

    @FormUrlEncoded
    @POST("/relationships")
    Call<RelationshipRespone> createReltionship(@Field(value = "idUser1") String idUser1,
                                                @Field(value = "idUser2") String idUser2,
                                                @Field(value = "idUserAction") String idUserAction,
                                                @Field(value = "status") String status);
    @FormUrlEncoded
    @PUT("/relationships/{idUser1}/{idUser2}")
    Call<Void> updateReltionship(@Path(value = "idUser1") String idUser1,
                                   @Path(value = "idUser2") String idUser2,
                                   @Field(value = "idUserAction") String idUserAction,
                                   @Field(value = "status") String status);

    @DELETE("/relationships/{idUser1}/{idUser2}")
    Call<Void> deleteFriend(@Path(value = "idUser1") String idUser1,
                              @Path(value = "idUser2") String idUser2);

    @GET("/relationships/getRelationship/{idUser1}/{idUser2}")
    Call<RelationshipRespone> getRelationship(@Path(value = "idUser1") String idUser1,
                                              @Path(value = "idUser2") String idUser2);
}
