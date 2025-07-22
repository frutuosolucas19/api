package br.udesc.controller.resources;

import java.util.List;

import javax.ws.rs.Path;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.udesc.controller.repositories.DenunciaRepository;
import br.udesc.controller.repositories.UsuarioRepository;
import br.udesc.dto.DenunciaRequest;
import br.udesc.model.Denuncia;
import br.udesc.model.Usuario;

@Path("/denuncia")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DenunciaResource {
    
    @Inject
    DenunciaRepository denunciaRepository;

    @Inject
    UsuarioRepository usuarioRepository;

    @GET
    @Path("/denuncias")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Denuncia> denuncias = denunciaRepository.listAll();
        return Response.ok(denuncias).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        return denunciaRepository.findByIdOptional(id).
                map(user -> Response.ok(user).build())
                .orElse(Response.status(404).build());
    }


   @POST
    @Path("/denuncia")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response criarDenuncia(DenunciaRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmailUsuario());
        if (usuario == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Usuário não encontrado").build();
        }

        Denuncia denuncia = new Denuncia();
        denuncia.setLocal(request.getLocal());
        denuncia.setProblema(request.getProblema());
        denuncia.setSugestao(request.getSugestao());
        denuncia.setImagem(request.getImagem());
        denuncia.setStatusAtual(request.getStatusAtual());
        denuncia.setHistorico(request.getHistorico());
        denuncia.setUsuario(usuario); 

        denunciaRepository.persist(denuncia);

        return Response.status(Response.Status.CREATED).build();
}

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Denuncia denuncia) {
        denunciaRepository.persist(denuncia);
        if (denunciaRepository.isPersistent(denuncia)) {
            return Response.status(200).build();
        }
        return Response.status(404).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, Denuncia denuncia) {
        Denuncia d = denunciaRepository.findById(id);
        d.setLocal(denuncia.getLocal());
        d.setProblema(denuncia.getProblema());
        d.setSugestao(denuncia.getSugestao());
        d.setUsuario(denuncia.getUsuario());
        d.setImagem(denuncia.getImagem());
        d.setStatusAtual(denuncia.getStatusAtual());
        d.setHistorico(denuncia.getHistorico());
        return Response.status(200).build();

    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteById(@PathParam("id") Long id) {
        boolean deleted = denunciaRepository.deleteById(id);
        return deleted ? Response.noContent().
                build() : Response.status(404).build();
    }
}
