import resource from "../resource/resource";

export const getAllUsers = async () => {
  let response = await resource.get("/admin/users");
  if (response.status !== 200) {
    throw new Error(response.data);
  } else {
    return await response.data;
  }
};

export const getLoggedInUser = async () => {
  let response = await resource.get("/users/me");
  if (response.status !== 200) {
    throw new Error(response.data);
  } else {
    return await response.data;
  }
};

export const updateUser = async user => {
  let response = await resource.put("/users/me", {
    name: user.name,
    email: user.email
  });
  if (response.status !== 200) {
    throw new Error(response.data);
  } else {
    return await response.data;
  }
};

export const changePassword = async cred => {
  let response = await resource.post("/users/change-password", {
    password: cred.password
  });
  if (response.status !== 200) {
    throw new Error(response.data);
  } else {
    return await response.data;
  }
};
