import resource from "@/resource/resource";
import { sendResponse } from "@/util/responseUtil.js";

export const getUsersOrder = async () => {
  let response = await resource.get("/orders/users/me");
  return sendResponse(response, 200);
};

export const getUsersOrderHistory = async () => {
  let response = await resource.get("/orders/users/me/history");
  return sendResponse(response, 200);
};

export const getUsersOrderReturnOverdue = async () => {
  let response = await resource.get("/orders/users/me/overdue/return");
  return sendResponse(response, 200);
};

export const getUsersOrderCollectionOverdue = async () => {
  let response = await resource.get("/orders/users/me/overdue/collect");
  return sendResponse(response, 200);
};

export const orderNewBook = async book => {
  let response = await resource.post("/orders", { bookId: book.id });
  return sendResponse(response, 201);
};
