import resource from "@/resource/resource";
import { sendResponse } from "@/util/responseUtil.js";

export const getAllUsers = async () => {
  let response = await resource.get("/admin/users");
  return sendResponse(response, 200);
};

export const getUser = async id => {
  let response = await resource.get(`/admin/users/${id}`);
  return sendResponse(response, 200);
};

export const saveUser = async user => {
  let response = await resource.post("/admin/users/", {
    name: user.name,
    email: user.email,
    username: user.username,
    userRoles: user.userRoles
  });
  return sendResponse(response, 201);
};

export const updateUser = async user => {
  let response = await resource.put(`/admin/users/${user.id}`, {
    name: user.name,
    email: user.email,
    enabled: user.enabled,
    userRoles: user.userRoles
  });
  return sendResponse(response, 200);
};

export const deleteUser = async id => {
  let response = await resource.delete(`/admin/users/${id}`);
  return sendResponse(response, 204);
};

export const getLoggedInUser = async () => {
  let response = await resource.get("/users/me");
  return sendResponse(response, 200);
};

export const updateMe = async user => {
  let response = await resource.put("/users/me", {
    name: user.name,
    email: user.email
  });
  return sendResponse(response, 200);
};

export const changePassword = async cred => {
  let response = await resource.post("/users/me/change-password", {
    password: cred.password
  });
  return sendResponse(response, 200);
};
