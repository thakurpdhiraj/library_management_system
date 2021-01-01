import resource from "../resource/resource";

export const login = async (cred) => {
  let response = await resource.post(
    "/login",
    new URLSearchParams({
      username: cred.username,
      password: cred.password,
    })
  );

  if (!response.status === 200) {
    throw new Error(response.data);
  } else {
    let user = await response.data;
    sessionStorage.setItem(
      "user",
      JSON.stringify({
        name: user.username,
        role: user.userRoles,
      })
    );
    return user;
  }
};

export const logout = async () => {
  let response = await resource.post("/logout");

  if (!response.status === 200) {
    throw new Error(response.data);
  } else {
    sessionStorage.removeItem("user");
  }
};
