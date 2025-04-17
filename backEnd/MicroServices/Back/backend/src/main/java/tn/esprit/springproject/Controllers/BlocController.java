package tn.esprit.springproject.Controllers;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springproject.Entities.Bloc;
import tn.esprit.springproject.Services.IBlocService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/bloc")
public class BlocController {
    private final IBlocService blocService;

    @GetMapping("/retrieve-all-bloc") // Fixed spelling
    public List<Bloc> getBlock() { // Renamed method for clarity
        return blocService.getBlocks();
    }

    @GetMapping("/retrieve-bloc/{bloc-id}")
    public Bloc retrieveBloc(@PathVariable("bloc-id") Long blockid) {
        return blocService.retrieveBloc(blockid);
    }

    @PostMapping("/add-bloc")
    public Bloc addBloc(@RequestBody Bloc b) {
        return blocService.addBloc(b);
    }

    @DeleteMapping("/remove-bloc/{bloc-id}")
    public void removeBloc(@PathVariable("bloc-id") Long bid) {
        blocService.deleteBloc(bid);
    }

    @PutMapping("/modify-block")
    public Bloc modifyBloc(@RequestBody Bloc b) {
        return blocService.modifyBloc(b);
    }
    @GetMapping("/u")
    public String getu(Authentication authentication){
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();

            // Extract email (if present in claims)
            String email = jwt.getClaim("email");

            // Extract roles (assuming roles are stored in "roles" claim)
            List<String> roles = jwt.getClaim("roles");

            return "User email: " + email + " | Roles: " + roles;
        }
        return "No user authenticated";
    }
}
