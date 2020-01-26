package com.baniya.cart.microservice.controller;

import com.baniya.cart.microservice.controller.impl.APIProxy;
import com.baniya.cart.microservice.controller.impl.CartProxy;
import com.baniya.cart.microservice.controller.impl.MerchantProxy;
import com.baniya.cart.microservice.dto.*;
import com.baniya.cart.microservice.entity.Cart;
import com.baniya.cart.microservice.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")

public class CartController {

    @Autowired
    CartService cartService;

    @Autowired
    APIProxy apiProxy;

    @Autowired
    CartProxy cartProxy;

    @Autowired
    MerchantProxy merchantProxy;

    @GetMapping("/message")
    public String print(){
        System.out.println("message");
        return "hi";
    }

    @PostMapping("/addToCart")
    public ResponseEntity<ResponseToCart> addToCart(@RequestBody AddToCarDTO addToCarDTO, @RequestHeader HttpHeaders headers)
    {
        String cartId=null;
        Object data = headers.get("guest");

        if(headers.get("Auth") == null){
            cartId = headers.get("guest").get(0);
        }
        else if(!(headers.get("Auth") == null) && !(headers.get("guest") ==null))
        {
            Optional<Cart> oldGuestCart = cartService.findCartByCartId(headers.get("guest").get(0));
            if(oldGuestCart.isPresent())
            {
                String userIdHeader = headers.get("Auth").get(0);
                UserProfile userProfile = apiProxy.getCurrentUser(userIdHeader);
                oldGuestCart.get().setCartId(userProfile.getId());

                cartService.save(oldGuestCart);
                cartService.deleteById(headers.get("guest").get(0));
            }
        }
        else
        {
            String userIdHeader = headers.get("Auth").get(0);
            UserProfile userProfile = apiProxy.getCurrentUser(userIdHeader);
            cartId = userProfile.getId();
        }

        ProductDTO newProduct = cartProxy.viewProductById(addToCarDTO.getProductId());
        MerchantProduct productDTO = merchantProxy.viewProductByProductIdAndMerchantId(addToCarDTO.getProductId(), addToCarDTO.getMerchantId());
        productDTO.setMerchantId(addToCarDTO.getMerchantId());
        double price = productDTO.getPrice();
        ResponseToCart responseToCart = new ResponseToCart();
        String productIdToBeAdded = addToCarDTO.getProductId();
        Optional<Cart> existingCartInDatabase = cartService.findCartByCartId(cartId);
        String merchantId = addToCarDTO.getMerchantId();


        if (existingCartInDatabase.isPresent()) {
            Cart existingCart = existingCartInDatabase.get();

                if (null == existingCart.getProductDTO()) {
                    existingCart.setProductDTO(new ArrayList<>());
                }
            List<ProductDTO> products = existingCart.getProductDTO();
            Optional<ProductDTO> productExist = products.stream().filter(productDTO1 -> productDTO1.getMerchantId().equals(merchantId))
                    .filter(productDTO1 -> productDTO1.getProductId().equals(productIdToBeAdded)).findFirst();


                if (productExist.isPresent() && !(productExist.get().getMerchantId().equals(merchantId))) {
                    newProduct.setMerchantId(productDTO.getMerchantId());
                    newProduct.setCounter(1);
                    newProduct.setPrice(productDTO.getPrice());
                    existingCart.getProductDTO().add(newProduct);
                    existingCart = cartService.save(existingCartInDatabase);
                } else if (productExist.isPresent() && (productExist.get().getMerchantId().equals(merchantId))) {
                    newProduct.setProductId(productDTO.getProductId());
                    newProduct.setMerchantId(productDTO.getMerchantId());
                    newProduct.setPrice(productDTO.getPrice());
                    productExist.get().setCounter(productExist.get().getCounter() + 1);
                    existingCart = cartService.save(existingCartInDatabase);
                } else {
                    newProduct.setProductId(productIdToBeAdded);
                    newProduct.setMerchantId(productDTO.getMerchantId());
                    newProduct.setPrice(price);
                    newProduct.setCounter(1);
                    existingCart.getProductDTO().add(newProduct);
                    existingCart = cartService.save(existingCartInDatabase);
                }

                responseToCart.setCartId(existingCart.getCartId());
            }
            else {
                Cart newCart = new Cart();
                newCart.setCartId(cartId);
                newProduct.setCounter(1);
                newProduct.setMerchantId(productDTO.getMerchantId());
                newCart.setProductDTO(Collections.singletonList(newProduct));
                newCart = cartService.insert(newCart);
                String id = newCart.getCartId();
                responseToCart.setCartId(cartId  );
            }
            return new ResponseEntity<ResponseToCart>(responseToCart,HttpStatus.CREATED);

}

    @GetMapping("/viewCart")
    public ResponseEntity<Cart> viewCart(@RequestHeader HttpHeaders headers) {
        String cartId = null;
        try {
            if (headers.get("Auth") == null) {
                cartId = headers.get("guest").get(0);
            } else if (!(headers.get("Auth") == null) && !(headers.get("guest") == null)) {
                Optional<Cart> oldGuestCart = cartService.findCartByCartId(headers.get("guest").get(0));
                if (oldGuestCart.isPresent()) {
                    String userIdHeader = headers.get("Auth").get(0);
                    UserProfile userProfile = apiProxy.getCurrentUser(userIdHeader);
                    cartId = userProfile.getId();
                    oldGuestCart.get().setCartId(cartId);
                    cartService.save(oldGuestCart);
                    cartService.deleteById(headers.get("guest").get(0));
                }
            } else {
                String userIdHeader = headers.get("Auth").get(0);
                UserProfile userProfile = (UserProfile) apiProxy.getCurrentUser(userIdHeader);
                cartId = userProfile.getId();
            }
            Optional<Cart> cart = cartService.findCartByCartId(cartId);
            if (cart.isPresent()) {
                Double total = cart.get().getProductDTO().stream().map(productDTO -> productDTO.getCounter() * productDTO.getPrice()).mapToDouble(Double::doubleValue).sum();
                cart.get().setTotal(total);
                cartService.save(cart);

                return new ResponseEntity<Cart>(cartService.findCartByCartId(cartId).get(), HttpStatus.OK);

            }
        } catch(Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
        }

            return  null;

    }


    @PostMapping("/updateCart")
    public ResponseEntity<ResponseToCart> updateCart(@RequestBody UpdateCartDTO updateCartDTO, @RequestHeader HttpHeaders headers)
    {
        Cart cart = new Cart();
        String cartId= null;
        if(headers.get("Auth")==null)
        {
            cartId = headers.get("guest").get(0);
        }
        else if(!(headers.get("Auth") == null) && !(headers.get("guest") ==null)) {
            Optional<Cart> oldGuestCart = cartService.findCartByCartId(headers.get("guest").get(0));
            if (oldGuestCart.isPresent()) {
                oldGuestCart.get().setCartId(headers.get("Auth").get(0));
            }
        }
        else
        {
            String userIdHeader =  headers.get("Auth").get(0);
            UserProfile userProfile = apiProxy.getCurrentUser(userIdHeader);
            cartId = userProfile.getId();
        }

        String productIdToBeUpdated;
        Integer countToBeUpdated;

        productIdToBeUpdated = updateCartDTO.getProductId();
        countToBeUpdated = updateCartDTO.getCounter();
        cart = cartService.findCartByCartId(cartId).get();
        Optional<ProductDTO> productUpdate = cart.getProductDTO().stream().filter(productDTO -> productDTO.getProductId().equals(productIdToBeUpdated)).findFirst();
        productUpdate.get().setCounter(countToBeUpdated);
        Cart cartUpdated =new Cart();
        Double total = cart.getProductDTO().stream().map(productDTO -> productDTO.getCounter() * productDTO.getPrice()).mapToDouble(Double::doubleValue).sum();
        cart.setTotal(total);
        cartUpdated = cartService.save(Optional.of(cart));

        ResponseToCart responseToCart = new ResponseToCart();
        responseToCart.setCartId(cart.getCartId());
        responseToCart.setTotal(cart.getTotal());

        return new ResponseEntity<ResponseToCart>(responseToCart,HttpStatus.CREATED);


    }


    @DeleteMapping("/delete/{productId}")
    public void deleteProduct(@RequestHeader HttpHeaders httpHeaders, @PathVariable("productId") String productId)
    {
        Cart cart = new Cart();
        String cartId = null;
        if(httpHeaders.get("Auth").equals("null"))
        {
            cartId = httpHeaders.get("guest").get(0);
        }
        else
        {
            String userIdHeader =  httpHeaders.get("Auth").get(0);
            UserProfile userProfile = apiProxy.getCurrentUser(userIdHeader);
            cartId = userProfile.getId();
        }

        cart = cartService.findCartByCartId(cartId).get();
        List<ProductDTO> products = cart.getProductDTO();
        cart.setProductDTO(products.stream().filter(productDTO-> !productDTO.getProductId().equals(productId)).collect(Collectors.toList()));

        cartService.save(Optional.of(cart));
    }




}
