import resource from "@/resource/resource";
import { sendResponse } from "@/util/responseUtil.js";

export const getUsersOrder = async () => {
  let response = await resource.get("/orders/me");
  return sendResponse(response, 200);
};

export const getUsersOrderHistory = async () => {
  let response = await resource.get("/orders/me/history");
  return sendResponse(response, 200);
};

export const getUsersOrderReturnOverdue = async () => {
  let response = await resource.get("/orders/me/overdue/return");
  return sendResponse(response, 200);
};

export const getUsersOrderCollectionOverdue = async () => {
  let response = await resource.get("/orders/me/overdue/collect");
  return sendResponse(response, 200);
};

export const orderNewBook = async book => {
  let response = await resource.post("/orders/me", {
    bookId: book.id,
    bookIsbn: book.isbn,
    bookName: book.name
  });
  return sendResponse(response, 201);
};

export const findOrders = async (type, id) => {
  var response;
  switch (type) {
    case "order":
      response = await resource.get(`/admin/orders/${id}`);
      break;
    case "user":
      response = await resource.get(`/admin/orders/users/${id}`);
      break;
    case "book":
      response = await resource.get(`/admin/orders/books/${id}`);
      break;
  }
  return sendResponse(response, 200);
};

export const collectOrder = async id => {
  let response = await resource.put(`/admin/orders/${id}/collect`);
  return sendResponse(response, 200);
};

export const returnOrder = async id => {
  let response = await resource.put(`/admin/orders/${id}/return`);
  return sendResponse(response, 200);
};

export const findOrdersHistoryOfUser = async userId => {
  let response = await resource.get(`/admin/orders/users/${userId}/history`);
  return sendResponse(response, 200);
};

export const findOrderOverdue = async (type, userType, userId) => {
  let response;
  switch (type) {
    case "collection":
      if (userType == "user") {
        response = await resource.get(
          `/admin/orders/users/${userId}/overdue/collect`
        );
      } else {
        response = await resource.get(`/admin/orders/overdue/collect`);
      }
      break;
    case "return":
      if (userType == "user") {
        response = await resource.get(
          `/admin/orders/users/${userId}/overdue/return`
        );
      } else {
        response = await resource.get(`/admin/orders/overdue/return`);
      }
      break;
  }
  return sendResponse(response, 200);
};
