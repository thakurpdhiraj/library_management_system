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
