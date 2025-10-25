package itmo.is.lab1.controllers;

import itmo.is.lab1.models.product.Organization;
import itmo.is.lab1.services.product.OrganizationService;
import itmo.is.lab1.services.product.requests.OrganizationRequest;
import itmo.is.lab1.services.product.responses.OrganizationResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/organizations")
public class OrganizationController extends GeneralController<OrganizationRequest, OrganizationResponse, Organization, OrganizationService> {
}

